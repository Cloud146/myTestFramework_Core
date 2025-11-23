package driverAdapter;

public final class AdapterHolder {
    private static final ThreadLocal<DriverAdapter> HOLDER = new ThreadLocal<>();

    private AdapterHolder() {}

    public static void set(DriverAdapter adapter) {
        HOLDER.set(adapter);
    }

    public static DriverAdapter get() {
        DriverAdapter adapter = HOLDER.get();
        if (adapter == null) {
            throw new IllegalStateException("DriverAdapter is not initialized in AdapterHolder");
        }
        return adapter;
    }

    public static void clear() {
        HOLDER.remove();
    }
}
