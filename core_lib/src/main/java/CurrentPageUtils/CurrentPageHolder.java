package CurrentPageUtils;

import FileUtils.PageReader;

public final class CurrentPageHolder {

    private volatile PageReader currentPage;

    public PageReader get() {
        if (currentPage == null) {
            throw new IllegalStateException("Страница не открыта: currentPage = null");
        }
        return currentPage;
    }

    public void set(PageReader page) {
        this.currentPage = page;
    }
}
