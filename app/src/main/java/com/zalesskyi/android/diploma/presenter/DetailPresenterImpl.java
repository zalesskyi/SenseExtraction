package com.zalesskyi.android.diploma.presenter;

import android.app.Application;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
                        doCreateTxtFileWithAbstract(pathToSourceTxt, abstractText);
                        // todo
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
                        doCreatePdfFileWithAbstract(pathToSourcePdf, abstractText);
                        // todo
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
                        doCreateDocFileWithAbstract(pathToSourceDoc, abstractText);
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
                    doCreateTxtFileWithAbstract(null, abstractText);
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
                                doCreateTxtFileWithAbstract(null, abstractText);
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

    @Override
    public void doCreateTxtFileWithAbstract(@Nullable String pathToSource, String abstractText) {
        String abstractFileName;
        if (pathToSource != null) {
            File source = new File(pathToSource);
            int endIndex = source.getName().lastIndexOf(".");
            String sourceName = source.getName().substring(0, endIndex);
            abstractFileName = getPathToAbstracts()
                    + sourceName + "_abstract.txt";
        } else {
            abstractFileName = getPathToAbstracts()
                    + UUID.randomUUID().toString() + "_abstract.txt";
        }
        try {
            File abstractFile = new File(abstractFileName);
            abstractFile.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(abstractFile), StandardCharsets.UTF_8);
            writer.write(abstractText);
            writer.close();
        } catch (IOException exc) {
            mView.showError(exc.getMessage());
        }
    }

    // todo encoding
    @Override
    public void doCreatePdfFileWithAbstract(String pathToSource, String abstractText) {
        com.itextpdf.text.Document doc = null;
        try {
            File source = new File(pathToSource);
            String abstractFileName = getPathToAbstracts() + source.getName()
                    .substring(0, source.getName().lastIndexOf(".")) + "_abstract.pdf";
            File pdf = new File(abstractFileName);
            pdf.createNewFile();

            doc = new com.itextpdf.text.Document();
            PdfWriter.getInstance(doc, new FileOutputStream(pdf));
            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(helvetica, 12, Font.NORMAL);
            Chunk chunk = new Chunk("",font);
            doc.add(chunk);
            doc.open();
            String[] parags = abstractText.split("\t");
            for (String parag : parags) {
                doc.add(new Paragraph(parag));
            }
        } catch (IOException | DocumentException exc) {
            mView.showError(exc.getMessage());
        }
        if (doc != null) {
            doc.close();
        }
    }

    @Override
    public void doCreateDocFileWithAbstract(String pathToSource, String abstractText) {
        try {
            File source = new File(pathToSource);
            String abstractFileName = getPathToAbstracts() + source.getName()
                    .substring(0, source.getName().lastIndexOf(".")) + "_abstract.docx";
            File abstractFile = new File(abstractFileName);
            abstractFile.createNewFile();
            Document doc = new Document();
            DocumentBuilder builder = new DocumentBuilder(doc);
            builder.write(abstractText);
            doc.save(abstractFileName);
        } catch (Exception exc) {
            mView.showError(exc.getMessage());
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
        String picFileName = doc.getOriginalFileName();
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
}
