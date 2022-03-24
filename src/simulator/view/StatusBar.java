package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 1L;
	
	private final static String TIME_MSG = "Time: ";
	private final static String EVENTS_MSG = "Event added ";
	
	private Controller _ctrl;
	
	private JLabel _timeInfo;
	private JLabel _eventsInfo;
	
	public StatusBar(Controller ctrl) {
		_timeInfo = new JLabel();
		_eventsInfo = new JLabel();
		_ctrl = ctrl;
		_ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		JPanel info = new JPanel(new FlowLayout(100));
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		info.add(_timeInfo);
		info.add(Box.createGlue());
		info.add(Box.createRigidArea(new Dimension(30, 10)));
		info.add(_eventsInfo);
		this.add(info, BorderLayout.WEST);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_timeInfo.setText(TIME_MSG + time);
		_eventsInfo.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_timeInfo.setText(TIME_MSG + time);
		_eventsInfo.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_timeInfo.setText(TIME_MSG + time);
		_eventsInfo.setText(EVENTS_MSG + "(" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_timeInfo.setText(TIME_MSG + time);
		_eventsInfo.setText("");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_timeInfo.setText(TIME_MSG + time);
		_eventsInfo.setText("Welcome!");
	}

	@Override
	public void onError(String err) {
		_eventsInfo = new JLabel(err);
	}
	
	

}
