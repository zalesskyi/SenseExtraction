package com.zalesskyi.android.diploma.presenter;

import android.app.Application;
import android.util.Log;

import com.aspose.words.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.zalesskyi.android.diploma.interactor.Interactor;
import com.zalesskyi.android.diploma.realm.RealmService;
import com.zalesskyi.android.diploma.utils.NetworkCheck;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        try {
            String source = extractTextFromPdf(pathToSourcePdf);
            mInteractor.toDoGetAbstract(source, 0)
                    .doOnRequest(l -> mView.showProgress())
                    .subscribe(response -> {

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
    public void doGetAbstractFromDoc(String pathToSourceDoc) {
        try {
            String source = extractTextFromDoc(pathToSourceDoc);
            mInteractor.toDoGetAbstract(source, 0)
                    .doOnRequest(l -> mView.showProgress())
                    .subscribe(response -> {

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

                }, err -> {
                    mView.showError(err.getMessage());
                }, () -> {
                    mView.hideProgress();
                });
    }

    @Override
    public void doGetAbstractFromUrl(String sourceTextUrl) {
        try {
            Observable.just(extractTextFromWebPage(sourceTextUrl))           // Извлекаем текст
                    .subscribeOn(Schedulers.io())                            // web-страницы
                    .observeOn(AndroidSchedulers.mainThread())               // в другом потоке.
                    .subscribe(source -> {                                   // Наблюдаем в главном потоке.
                        mInteractor.toDoGetAbstract(source, 0)     // После извлечения отправляем
                                .doOnRequest(l -> mView.showProgress())      // исходный текст web-страницы на сервер.
                                .subscribe(response -> {

                                }, err -> {
                                    mView.showError(err.getMessage());
                                }, () -> {
                                    mView.hideProgress();
                                });
                    });
        } catch (IOException exc) {
            mView.showError(exc.getMessage());
        }

    }

    @Override
    public void doGetAbstractFromClipboard() {

    }

    /**
     * Извлечение текста из PDF-документа.
     *
     * @param pathToPdf путь к pdf-документу
     * @return текст pdf-документа
     *
     * @throws IOException
     */
    private String extractTextFromPdf(String pathToPdf) throws IOException {
        StringBuilder source = new StringBuilder("");
        PdfReader reader = new PdfReader(pathToPdf);
        int pagesNum = reader.getNumberOfPages();

        for (int i = 0; i < pagesNum; i++) {
            source.append(PdfTextExtractor.getTextFromPage(reader, i + 1));
        }
        reader.close();
        return source.toString();
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
    private String extractTextFromWebPage(String url) throws IOException {
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
        return source.toString();
    }
}
