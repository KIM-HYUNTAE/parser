package SW_visualization;
import java.util.Vector;

public abstract class Player {
	protected Vector<Vector<Character>> hand = new Vector<Vector<Character>>();
	protected Vector<Vector<Character>> communityCard = new Vector<Vector<Character>>();
	protected int chips;
	protected int blind;
	protected String name = "sample Player";
	
	public Player(int chips, int blind) {
		this.chips = chips;
		this.blind = blind;
	}
	
	public void changeBlind(int blind) { this.blind = blind; }
	
	public void giveCard(Vector<Character> card) { hand.add(card); }
	
	public final Vector<Vector<Character>> getHand() {return hand;}
	
	public final int getChip() { return chips; }
	
	public final String getName() { return name; }
	
	public final void validateChip(int validatedChip) { this.chips = validatedChip; }
	
	public void giveChip(int wonChip) { this.chips += wonChip; }
	
	public void giveCommunityCard(Vector<Character> card) { communityCard.add(card); }
	
	public final void reset() {
		this.hand.clear();
		this.communityCard.clear();
	}
	
	/* �Լ��̸�: bet
	 * �Լ����: betToCall ���� ���ų� �� ū ���� �����Ͽ� ������ �����Ѵ�.
	 *     betToCall ������ ���� ���� �Է��� ��� die �������� �����Ѵ�.
	 *     isFirstBet�� true �� ��� Small Blind(blind ��)Ȥ�� Big Blind(blind*2�� �ش��ϴ� ��) �� �� �����ؾ��Ѵ�.
	 *     betToCall ���� 0�̸� isFirstBet�� false�� ���, 0�� �����Ͽ� check�� �� �� �ְ� ���� �Ϸ��� SB �̻� �����ؾ��Ѵ�.
	 *     �� chips �̻����� ������ �� ������, �Լ��� ����Ǳ��� bet�� ��ŭ�� ���� �� chips���� ���ؾ� �Ѵ�.
	 *     ���� Ȥ�� ���������� ����ġ�� ���� �� �� die ó�� �Ǹ� Ĩ�� ������ �����ȴ�.
	 * �ۼ���: ������
	 */
	public abstract int bet(boolean isFirstBet, int maxBet, int betToCall);
}
