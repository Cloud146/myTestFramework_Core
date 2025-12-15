package driverAdapter;

public interface DriverAdapterFactory {

    String getEngineName();

    DriverAdapter create();
}