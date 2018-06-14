package com.zalesskyi.android.diploma.view.main_operation.listeners;

import com.zalesskyi.android.diploma.realm.Abstract;

public interface ItemListener {

    void open(Abstract item);
    void share(Abstract item);
    void star(Abstract item);
    void openWith(Abstract item);
    void remove(Abstract item);
    void openSource(Abstract item);

}
