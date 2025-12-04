package CurrentPageUtils;

import FileUtils.PageReader;

public final class CurrentPageHolder {

    private final ThreadLocal<PageReader> currentPage = new ThreadLocal<>();

    public PageReader get() {
        PageReader page = currentPage.get();
        if (page == null) {
            throw new IllegalStateException("Страница не открыта: currentPage = null (Thread: " + Thread.currentThread().getId() + ")");
        }
        return page;
    }

    public void set(PageReader page) {
        this.currentPage.set(page);
    }

    // НОВОЕ: Нужно для очистки памяти после теста
    public void remove() {
        this.currentPage.remove();
    }
}
