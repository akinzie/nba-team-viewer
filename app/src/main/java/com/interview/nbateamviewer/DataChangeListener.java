package com.interview.nbateamviewer;

public interface DataChangeListener {
    /**
     * The function that will be invoked when the {@link DataModel} has updated data available.
     *
     * @see com.interview.nbateamviewer.DataModel#setDataChangeListener(DataChangeListener)
     */
    void onDataChanged();
}
