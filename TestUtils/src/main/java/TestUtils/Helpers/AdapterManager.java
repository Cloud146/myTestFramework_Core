package TestUtils.Helpers;

import driverAdapter.DriverAdapter;
import driverAdapter.DriverAdapterFactory;
import java.util.*;

public final class AdapterManager {

    private AdapterManager() {}

    public static DriverAdapter createAdapter(String engine) {
        return ServiceLoader.load(DriverAdapterFactory.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(f -> f.getEngineName().equalsIgnoreCase(engine))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No adapter found for engine: " + engine))
                .create();
    }

    public static void closeAdapter(DriverAdapter adapter) {
        if (adapter != null) {
            adapter.close();
        }
    }
}

