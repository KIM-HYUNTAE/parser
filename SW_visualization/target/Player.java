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
	
	/* 함수이름: bet
	 * 함수기능: betToCall 값과 같거나 더 큰 값을 제시하여 베팅을 진행한다.
	 *     betToCall 값보다 작은 값을 입력한 경우 die 선언으로 간주한다.
	 *     isFirstBet이 true 인 경우 Small Blind(blind 값)혹은 Big Blind(blind*2에 해당하는 값) 둘 중 배팅해야한다.
	 *     betToCall 값이 0이며 isFirstBet이 false인 경우, 0을 배팅하여 check를 할 수 있고 베팅 하려면 SB 이상 배팅해야한다.
	 *     내 chips 이상으로 배팅할 수 없으며, 함수가 종료되기전 bet한 만큼의 값을 내 chips에서 제해야 한다.
	 *     오류 혹은 부정행위로 오류치가 배팅 될 시 die 처리 되며 칩의 개수가 조정된다.
	 * 작성자: 박찬솔
	 */
	public abstract int bet(boolean isFirstBet, int maxBet, int betToCall);
}
