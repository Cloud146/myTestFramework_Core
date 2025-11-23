package CurrentPageUtils;

public final class PageContext {

    private static final CurrentPageHolder HOLDER = new CurrentPageHolder();

    private PageContext() {}

    public static CurrentPageHolder getHolder() {
        return HOLDER;
    }
}
