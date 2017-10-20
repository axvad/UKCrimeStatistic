package ru.sam.ukcrimestat;

/**
 * Callback interface: send result from Dialog to Application (by getAplication())
 */

public interface CrimeEventsListener {
    public abstract void onReciveCrimeData();
    public abstract void onReciveCrimeDataError(String message);
}
