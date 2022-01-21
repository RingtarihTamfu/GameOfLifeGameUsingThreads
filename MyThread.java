package com.company;

public class MyThread implements Runnable {
    private iCell cellCopy;
    MyThread(iCell cellRun) {
        cellCopy = cellRun;
    }
    @Override
    public void run() {
        cellCopy.determine();;
        cellCopy.update();;
    }
}
