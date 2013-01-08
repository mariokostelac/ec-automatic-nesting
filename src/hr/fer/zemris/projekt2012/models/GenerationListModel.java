package hr.fer.zemris.projekt2012.models;

import java.awt.Polygon;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class GenerationListModel extends AbstractListModel<Generation> {

	private static final long serialVersionUID = 1L;

	private List<Generation> list;

	public GenerationListModel(List<Generation> list) {
		this.list = list;

		ListDataListener[] listeners = this.getListDataListeners();

		ListDataEvent event = new ListDataEvent(this,
				ListDataEvent.INTERVAL_ADDED, 0, this.getSize() - 1);

		for (ListDataListener listener : listeners) {
			listener.intervalAdded(event);
		}
	}

	public void notifyOfAddition() {
		ListDataListener[] listeners = this.getListDataListeners();

		ListDataEvent event = new ListDataEvent(this,
				ListDataEvent.INTERVAL_ADDED, 0, this.getSize() - 1);

		for (ListDataListener listener : listeners) {
			listener.intervalAdded(event);

		}

	}

	@Override
	public Generation getElementAt(int index) {
		return this.list.get(index);
	}

	@Override
	public int getSize() {
		return this.list.size();
	}

}
