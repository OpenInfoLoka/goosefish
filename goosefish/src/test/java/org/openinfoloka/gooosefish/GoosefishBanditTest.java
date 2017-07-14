package org.openinfoloka.gooosefish;

import java.util.ArrayList;
import java.util.List;

import org.openinfoloka.goosefish.GoosefishBandit;

public class GoosefishBanditTest {
	
	List<Host> hosts = new ArrayList<>();
		
	private void createDistribution() {
		
	}
	
	@Test
	public void badit() {
		String url = "http://topicpage/home";
		GoosefishBandit gb = new GoosefishBandit();
		float score = gb.getScore(url);
	}
}
