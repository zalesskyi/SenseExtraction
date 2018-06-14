package com.zalesskyi.android.diploma.presenter;

import android.app.Application;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;
import com.zalesskyi.android.diploma.view.detail_operation.activities.DetailActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;


/*
    todo Для презентации:
        1) Web - https://www.osp.ru/lan/2003/01/137081/
        2) Doc - just.docx (История Битлз)
        3) Pdf - Poltava_battle.pdf
 */
public class DetailPresenterImpl extends BasePresenter
        implements PresenterContract.DetailPresenter {

    private static final String TAG = "DetailPresenter";

    public DetailPresenterImpl(Application application, NetworkCheck networkCheck
            , Interactor interactor, RealmService realmService) {
        super.mApplication = application;
        super.mNetworkCheck = networkCheck;
        super.mInteractor = interactor;
        super.mRealmService = realmService;
    }

    @Override
    public void doGetAbstractFromTxt(String pathToSourceTxt) {
        try (Scanner fileScanner = new Scanner(new FileInputStream(pathToSourceTxt))) {
            StringBuilder source = new StringBuilder("");
            while (fileScanner.hasNextLine()) {
                source.append(fileScanner.nextLine());
            }
            mInteractor.toDoGetAbstract(source.toString(), 0)
                    .doOnRequest(l -> mView.showProgress())
                    .subscribe(response -> {
                        String abstractText = response.getAbstract();
                        String abstractFileName = doCreateTxtFileWithAbstract(pathToSourceTxt, abstractText);
                        putAbstractIntoRealm(abstractFileName, pathToSourceTxt, Abstract.TXT_TYPE);
                        ((DetailActivity) mView).displayAbstractFile(abstractFileName);
                    }, err -> {
                        mView.showError(err.getMessage());
                    }, () -> {
                        mView.hideProgress();
                    });
        } catch (IOException exc) {
            mView.showError(exc.getMessage());
        }
    }

    @Override
    public void doGetAbstractFromPdf(String pathToSourcePdf) {
        extractTextFromPdf(pathToSourcePdf)                                 // извлечение текста из PDF файла
                .subscribeOn(Schedulers.computation())
                .doOnRequest(l -> mView.showProgress())
                .subscribe(textFromPdf -> {                                 // после получения текста
                    Log.i(TAG, "from PDF: " + textFromPdf);            // отправление его на сервер
                    mInteractor.toDoGetAbstract(textFromPdf, 0)
                    .doOnRequest(l -> mView.showProgress())
                    .subscribe(response -> {
                        String abstractText = response.getAbstract();
                        String abstractPath = doCreateTxtFileWithAbstract(pathToSourcePdf, abstractText);
                        putAbstractIntoRealm(abstractPath, pathToSourcePdf, Abstract.PDF_TYPE);
                        ((DetailActivity) mView).displayAbstractFile(abstractPath);
                    }, err -> {
                        mView.showError(err.getMessage());
                    }, () -> {
                        mView.hideProgress();
                    });
        }, err -> mView.showError(err.getMessage()));
    }

    @Override
    public void doGetAbstractFromDoc(String pathToSourceDoc) {
        try {
            String source = extractTextFromDoc(pathToSourceDoc);
            Log.i(TAG, "source");
            mInteractor.toDoGetAbstract(source, 0)
                    .doOnRequest(l -> mView.showProgress())
                    .subscribe(response -> {
                        String abstractText = response.getAbstract();
                        String abstractPath = doCreateTxtFileWithAbstract(pathToSourceDoc, abstractText);
                        putAbstractIntoRealm(abstractPath, pathToSourceDoc, Abstract.DOC_TYPE);
                        ((DetailActivity) mView).displayAbstractFile(abstractPath);
                    }, err -> {
                        mView.showError(err.getMessage());
                    }, () -> {
                        mView.hideProgress();
                    });
        } catch (Exception exc) {
            mView.showError(exc.getMessage());
        }
    }

    @Override
    public void doGetAbstractFromString(String sourceText) {
        mInteractor.toDoGetAbstract(sourceText, 0)
                .doOnRequest(l -> mView.showProgress())
                .subscribe(response -> {
                    String abstractText = response.getAbstract();
                    String abstractPath = doCreateTxtFileWithAbstract(null, abstractText);
                    putAbstractIntoRealm(abstractPath, null, Abstract.CLIPBOARD_TYPE);
                    ((DetailActivity) mView).displayAbstractFile(abstractPath);
                }, err -> {
                    mView.showError(err.getMessage());
                }, () -> {
                    mView.hideProgress();
                });
    }

    @Override
    public void doGetAbstractFromUrl(String sourceTextUrl) {
        extractTextFromWebPage(sourceTextUrl)                            // Извлекаем текст
                .subscribeOn(Schedulers.io())                            // web-страницы
                .observeOn(AndroidSchedulers.mainThread())               // в другом потоке.
                .subscribe(source -> {                                   // Наблюдаем в главном потоке.
                    mInteractor.toDoGetAbstract(source, 0)     // После извлечения отправляем
                            .doOnRequest(l -> mView.showProgress())      // исходный текст web-страницы на сервер.
                            .subscribe(response -> {
                                String abstractText = response.getAbstract();
                                String abstractPath = doCreateTxtFileWithAbstract(null, abstractText);
                                putAbstractIntoRealm(abstractPath, sourceTextUrl, Abstract.WEB_TYPE);
                                ((DetailActivity) mView).displayAbstractFile(abstractPath);
                            }, err -> mView.showError(err.getMessage()),
                                    () -> mView.hideProgress());
                });

    }

    @Override
    public Observable<Bitmap> doGetDocPageImage(String path, int pageNum) {
        try {
            return Observable.just(getDocPageImage(path, pageNum))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } catch (Exception exc) {
            return Observable.error(exc);
        }
    }

    @Override
    public Observable<Integer> doGetWordDocumentPageCount(String pathToDoc) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(new Document(pathToDoc).getPageCount());
            } catch (Exception exc) {
                subscriber.onError(exc);
            }
            subscriber.onCompleted();
        });
    }

    @Nullable
    @Override
    public String doCreateTxtFileWithAbstract(@Nullable String pathToSource, String abstractText) {
        try {
            String abstractFileName = getPathToAbstract(pathToSource);
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(abstractFileName), StandardCharsets.UTF_8);
            writer.write(abstractText);
            writer.close();
            return abstractFileName;
        } catch (IOException exc) {
            mView.showError(exc.getMessage());
            return null;
        }
    }

    /**
     * @return Текст из буфера обмена
     */
    public CharSequence getTextFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) mApplication.getSystemService(CLIPBOARD_SERVICE);
        return clipboard.getPrimaryClip().getItemAt(0).getText();
    }

    /**
     * Вставка записи в БД.
     *
     * @param pathToAbstract путь к файлу с рефератом.
     * @param pathToSource путь к исходному файлу (с случае с URL - URL)
     * @param typeOfSource тип исходного файла
     */
    private void putAbstractIntoRealm(String pathToAbstract, String pathToSource, int typeOfSource) {
        Abstract newAbstract = new Abstract.Builder()
                .setPathToSource(pathToSource == null ? "" : pathToSource)
                .setPathToAbstract(pathToAbstract)
                .setPathToSourceThumb(typeOfSource == Abstract.DOC_TYPE
                        || typeOfSource == Abstract.PDF_TYPE ?
                        getPathToThumbnails() + getFileNameWithoutExtension(pathToSource) + ".jpeg" : "")
                .setPathToImagesIfDoc(typeOfSource == Abstract.DOC_TYPE ?
                        getPathToDocPageImages() + getFileNameWithoutExtension(pathToSource) : "")
                .setCreationDate(new Date().getTime())
                .setIsFavorite(false)
                .setSyncedWithCloud(false)
                .setType(typeOfSource)
                .build();
        mRealmService.addObject(newAbstract, Abstract.class)
                .subscribe(abs -> {
                        Log.i(TAG, "abstract " + abs.toString() + "was added into realm");
                    }, err -> Log.e(TAG, err.getMessage()));
    }

    /**
     * Извлечение текста из PDF-документа.
     *
     * @param pathToPdf путь к pdf-документу
     * @return текст pdf-документа
     *
     * @throws IOException
     */
    private Observable<String> extractTextFromPdf(String pathToPdf) {
        return Observable.create(subscriber -> {
            try {
                StringBuilder source = new StringBuilder("");
                PdfReader reader = new PdfReader(pathToPdf);
                int pagesNum = reader.getNumberOfPages();

                for (int i = 0; i < pagesNum; i++) {
                    source.append(PdfTextExtractor.getTextFromPage(reader, i + 1));
                }
                reader.close();
                subscriber.onNext(source.toString());
            } catch (IOException exc) {
                subscriber.onError(exc);
            }
            subscriber.onCompleted();
        });
    }

    /**
     * Извлечение текста из DOC-файла.
     *
     * @param pathToDoc путь к doc-файлу
     * @return текст doc-файла
     *
     * @throws Exception
     */
    private String extractTextFromDoc(String pathToDoc) throws Exception {
        Document doc = new Document(pathToDoc);
        return doc.getText();
    }

    /**
     * Извлечение текста из web-страницы.
     *
     * @param url URL web-страницы
     * @return текст web-страницы
     *
     * @throws Exception
     */
    private Observable<String> extractTextFromWebPage(String url) {
        return Observable.create(subscriber -> {
            try {
                Connection connection = Jsoup.connect(url);
                connection.postDataCharset("UTF-8");
                org.jsoup.nodes.Document doc = connection.get();
                Elements parags = doc.select("p");
                StringBuilder source = new StringBuilder("");
                for (Element p : parags) {
                    Log.i(TAG, p.text());
                    source.append(p.text()).append(".\n");
                }
                Log.i(TAG, "text: " + source);
                subscriber.onNext(source.toString());
                subscriber.onCompleted();
            } catch (IOException exc) {
                subscriber.onError(exc);
            }
        });

    }

    /**
     * Обертывается в Observable в методе doGetDocPageImage().
     *
     * Если файл с изображением страницы уже существует, то возвращается его bitmap.
     * Если не существует, то он создается, а затем возвращается его bitmap.
     *
     * @param pathToDoc путь к doc-файлу.
     * @param page номер страницы.
     * @return картинку страницы doc-файла.
     */
    private Bitmap getDocPageImage(String pathToDoc, int page) throws Exception {
        Document doc = new Document(pathToDoc);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        String picFileName = getFileNameWithoutExtension(pathToDoc);
        picFileName = picFileName.replaceAll("/", "_");
        String pathToPic = getPathToDocPageImages() + picFileName + "/" + page;
        File file = new File(pathToPic);
        if (file.exists()) {
            return BitmapFactory.decodeFile(pathToPic, opts);
        }
        ImageSaveOptions options = new ImageSaveOptions(SaveFormat.JPEG);
        options.setJpegQuality(100);
        options.setPageIndex(page);
        options.setPageCount(1);
        doc.save(pathToPic, options);
        return BitmapFactory.decodeFile(pathToPic, opts);
    }

    private String getPathToAbstract(String pathToSource) throws IOException {
        String abstractFileName;
        if (pathToSource != null) {
            String sourceName = getFileNameWithoutExtension(pathToSource);
            abstractFileName = getPathToAbstracts()
                    + sourceName + "_abstract.txt";
        } else {
            abstractFileName = getPathToAbstracts()
                    + UUID.randomUUID().toString() + "_abstract.txt";
        }
        File file = new File(abstractFileName);
        file.createNewFile();
        return abstractFileName;
    }

    /**
     * Получение имени файла.
     *
     * @param path путь к файлу
     * @return имя файла без расширения
     */
    private String getFileNameWithoutExtension(String path) {
        File source = new File(path);
        int endIndex = source.getName().lastIndexOf(".");
        return source.getName().substring(0, endIndex);
    }
}
