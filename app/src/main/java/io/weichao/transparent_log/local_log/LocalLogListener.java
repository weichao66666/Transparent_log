package io.weichao.transparent_log.local_log;

public interface LocalLogListener {
    void hideLog();

    void showLog();

    void clearLog();

    void printLog(String msg);

    void printLog(int level, String msg);
}