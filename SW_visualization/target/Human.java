package SW_visualization;
import java.util.Scanner;
import java.util.Vector;

public class Human extends Player{
	private String [] numbers = {"Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","JACK","QUEEN","KING","ACE"};
	private String [] patterns = {"Club","Diamond","Heart","Spade"};
    private Vector<Character> pattern = new Vector<Character>();
    private Vector<Character> number = new Vector<Character>();
    Scanner sc = new Scanner(System.in);
	
	public Human(int chips, int blind) {
		super(chips, blind);
		
		System.out.print("����� �̸��� �����Դϱ�? :");
		this.name = sc.nextLine();
		
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
	public void changeBlind(int blind) { 
		System.out.println("Dealer : blind�� �ٲߴϴ�.");
		System.out.println(this.blind+"->"+blind);
		this.blind = blind;
	}
	
	@Override
	public void giveCard(Vector<Character> card) { 
		hand.add(card);
		System.out.println("Dealer : ī�带 �帮�ڽ��ϴ�.");
		for(Vector<Character> eachCard : hand) {
			System.out.println("["+patterns[pattern.indexOf(eachCard.get(0))]+" "+numbers[number.indexOf(eachCard.get(1))]+"]");
		}
	}
	
	@Override
	public void giveChip(int wonChip) {
		this.chips += wonChip;
		System.out.println("Dealer : Ĩ�� �帮�ڽ��ϴ�.");
		System.out.println("���� Ĩ : "+wonChip+"|��ü Ĩ : "+chips);
	}

	@Override
	public int bet(boolean isFirstBet, int maxBet, int betToCall) {
		int bet;
		System.out.println("�� �� : "+"["+patterns[pattern.indexOf(hand.get(0).get(0))]+" "+numbers[number.indexOf(hand.get(0).get(1))]+"]"+"["+patterns[pattern.indexOf(hand.get(1).get(0))]+" "+numbers[number.indexOf(hand.get(1).get(1))]+"]");
		if(isFirstBet) {
			System.out.println("���� ����ε� : "+blind+" | �� ����ε� : "+blind*2);

		}else {
			System.out.println("�ּ� ���� : "+betToCall+" | �ִ� ���� : "+maxBet);

		}
		System.out.print("����: ���� �Ͻðڽ��ϱ�? >>> ");
		bet = sc.nextInt();
		chips -= bet;
		return bet;
	}

}

