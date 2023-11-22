package SW_visualization;

import java.util.Vector;

public class Dummy extends Player{
    private Vector<Character> pattern = new Vector<Character>();
    private Vector<Character> number = new Vector<Character>();
	
	public Dummy(int chips, int blind) {
		super(chips, blind);
		
		this.name = "Dummy";
		
		char [] patterns = {'C','D','H','S'};
		for(char pattern : patterns) {
			this.pattern.add(pattern);
		}
	    char [] numbers = {'2','3','4','5','6','7','8','9','T','J','Q','K','A'};
	    for(char number : numbers) {
			this.number.add(number);
		}
	}

	@Override
	public int bet(boolean isFirstBet, int maxBet, int betToCall) {
		int bet = 0;
		if(betToCall == 0) {
			bet = blind;
		}else {
			bet = betToCall;
		}
		chips -= bet;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return bet;
	}

}
