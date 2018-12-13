package io.weichao.transparent_log.local_log;

public interface LocalLogFragmentView {
    void hide();

    void show();

    void clear();

    void log(int level, String msg);
}