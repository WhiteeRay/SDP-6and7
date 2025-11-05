package demo;

import core.WeatherStation;
import strategies.*;
import observers.*;

public class WeatherNotificationSystem {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Weather Notification System Starting...");

        WeatherStation station = new WeatherStation(new RealTimeUpdateStrategy());

        WeatherObserver phoneApp = new MobileApp("WeatherPro");
        WeatherObserver desktop = new DesktopDisplay("Living Room");
        WeatherObserver smartHome = new SmartHomeSystem();

        station.registerObserver(phoneApp);
        station.registerObserver(desktop);
        station.registerObserver(smartHome);

        System.out.println("DEMONSTRATION START");

        System.out.println("DEMO 1: Real-time Sensor Updates");
        for (int i = 0; i < 2; i++) {
            station.performUpdate();
            Thread.sleep(1000);
        }

        System.out.println("SWITCHING STRATEGY...");
        station.setUpdateStrategy(new ScheduledUpdateStrategy());

        System.out.println("DEMO 2: Scheduled Batch Updates");
        for (int i = 0; i < 3; i++) {
            station.performUpdate();
            Thread.sleep(1000);
        }

        System.out.println("SWITCHING STRATEGY...");
        ManualUpdateStrategy manualStrategy = new ManualUpdateStrategy();
        station.setUpdateStrategy(manualStrategy);

        System.out.println("DEMO 3: Manual Input Updates");
        manualStrategy.setManualData(19.5, 75.2, 1012.3);
        station.performUpdate();

        System.out.println("UPDATING MANUAL DATA...");
        manualStrategy.setManualData(27.8, 45.6, 1018.7);
        station.performUpdate();

        System.out.println("OBSERVER MANAGEMENT DEMO");
        station.removeObserver(desktop);
        System.out.println("Active observers: " + station.getObserverCount());
        station.performUpdate();

        System.out.println("ADDING NEW OBSERVER");
        WeatherObserver tabletApp = new MobileApp("WeatherTablet");
        station.registerObserver(tabletApp);
        station.performUpdate();

        System.out.println("DEMONSTRATION COMPLETE");
        System.out.println("Final observer count: " + station.getObserverCount());
        System.out.println("Final strategy: " + station.getCurrentStrategy());
    }
}