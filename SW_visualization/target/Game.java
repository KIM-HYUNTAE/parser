import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import com.chansol.player.Player;
import com.chansol.player.Human;

import com.chansol.player.Player;
import com.chansol.player.Human;

public class Game {
	private Vector<Vector<Character>> dealerShoe = new Vector<Vector<Character>>();
	private Vector<Vector<Character>> communityCards = new Vector<Vector<Character>>();
	private Vector<Vector<Character>> burn = new Vector<Vector<Character>>();
	private Vector<Player> players = new Vector<Player>();
	private Vector<Character> patterns = new Vector<Character>();
	private Vector<Character> numbers = new Vector<Character>();
	private Vector<Player> winner = new Vector<Player>();
	private HashMap<String, Integer> pot = new HashMap<String, Integer>();
	private HashMap<String, Integer> chipCheck = new HashMap<String, Integer>();
	private Vector<Player> onBet = new Vector<Player>();
	private int blind;

	private String[] combiP = { "Top", "One Pair", "Two Pair", "Tripple", "Straight", "Flush", "Full House",
			"Four Card", "Straight Flush" };
	private String[] numberP = { "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "JACK",
			"QUEEN", "KING", "ACE" };
	private String[] patternP = { "Club", "Diamond", "Heart", "Spade" };

	public static void main(String[] args) {
		Game game = new Game();
		game.setBlind(1);
		game.addPlayer(new Human(50, 1));
		game.addPlayer(new Human(50, 1));
		game.game();
	}

	public Game() {
		char[] patterns = { 'C', 'D', 'H', 'S' };
		for (char pattern : patterns) {
			this.patterns.add(pattern);
		}
		char[] numbers = { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };
		for (char number : numbers) {
			this.numbers.add(number);
		}
	}

	public void addPlayer(Player player) {
		this.players.add(player);
		pot.put(player.getName(), 0);
		chipCheck.put(player.getName(), player.getChip());
	}

	public void setBlind(int blind) {
		this.blind = blind;
	}

	public void game() {
		int bank;
		if (winner.isEmpty()) {
			bank = 0;
		} else {
			bank = players.indexOf(winner.get(0));
		}

		onBet.clear();
		for (int i = 0; i < players.size(); i++) {
			onBet.add(players.get((i + bank) % players.size()));
		}
		winner.clear();

		boolean isBetting = true;
		boolean isGaming = true;
		shuffle();
		deal();
		int result = bet();
		if (result == 1) {
			isBetting = false;
		}

		if (result == 2) {
			isBetting = false;
			isGaming = false;
		}

		if (isGaming) {
			System.out.println("Flop");
			burn();
			communityCardsDeal(3);
			for (Player player : players) {
				player.giveCommunityCard(communityCards.get(0));
				player.giveCommunityCard(communityCards.get(1));
				player.giveCommunityCard(communityCards.get(2));
			}

			if (isBetting) {
				result = bet();
				if (result == 1) {
					isBetting = false;
				}

				if (result == 2) {
					isBetting = false;
					isGaming = false;
				}
			}
		}

		if (isGaming) {
			System.out.println("Turn");
			burn();
			communityCardsDeal(1);
			for (Player player : players) {
				player.giveCommunityCard(communityCards.get(3));
			}
			if (isBetting) {
				result = bet();
				if (result == 1) {
					isBetting = false;
				}

				if (result == 2) {
					isBetting = false;
					isGaming = false;
				}
			}
		}

		if (isGaming) {
			System.out.println("River");
			burn();
			communityCardsDeal(1);
			for (Player player : players) {
				player.giveCommunityCard(communityCards.get(4));
			}
			if (isBetting) {
				result = bet();
				if (result == 1) {
					isBetting = false;
				}

				if (result == 2) {
					isBetting = false;
					isGaming = false;
				}
			}
		}

		if (isGaming) {
			System.out.println("승부");
			match();
		}

		validateChips();
		cleanTable();
		viewSituation();
	}

	private void shuffle() {
		dealerShoe.clear();
		for (char pattern : patterns) {
			for (char number : numbers) {
				Vector<Character> card = new Vector<Character>();
				card.add(pattern);
				card.add(number);
				dealerShoe.add(card);
			}
		}
		Collections.shuffle(dealerShoe);
	}

	private void deal() {
		for (int i = 0; i < 2; i++) {
			for (Player player : players) {
				player.giveCard(dealerShoe.firstElement());
				dealerShoe.remove(0);
			}
		}
	}

	/*
	 * return값을 통해 베팅 진행 결과 전달 0: 게임 진행, 1: All-in 발생, 2: 게임 종료
	 */
	private int bet() {
		int totalBet = 0;
		boolean isFirstBet = true;
		boolean isBetDone = false;
		boolean isAllIn = false;
		while (true) {
			for (Player player : onBet) {
				int maxBet = 1000;
				for (Player p : onBet) {
					if (p.getChip() < maxBet) {
						maxBet = p.getChip();
					}
				}
				int betHoCall = totalBet - pot.get(player.getName());
				int nowBet = player.bet(isFirstBet, maxBet, betHoCall);
				boolean betSuccess = true;
				if (betHoCall > nowBet) {
					// 필요 베팅양 보다 적게 배팅했을 시 die 처리
					betSuccess = false;
					onBet.remove(player);
				} else if (chipCheck.get(player.getName()) < nowBet) {
					// 칩 수를 초과해서 배팅했을 시 die 처리
					betSuccess = false;
					onBet.remove(player);
				} else if (isFirstBet && (nowBet % blind != 0 || nowBet > blind * 2)) {
					// 블라인드 배팅이 아닌경우 die 처리
					betSuccess = false;
					onBet.remove(player);
				}

				if (betSuccess) {
					pot.replace(player.getName(), pot.get(player.getName()) + nowBet);
					chipCheck.replace(player.getName(), chipCheck.get(player.getName()) - nowBet);
					if (chipCheck.get(player.getName()) == 0) {
						isAllIn = true;
					}
					if (betHoCall < nowBet) {
						// 레이즈 한 상황
						totalBet += nowBet;
					}
				} else {
					player.giveChip(nowBet);
				}

				boolean betCheck = true;
				int betChecker = -1;
				for (Player p : onBet) {
					if (pot.get(p.getName()) != betChecker) {
						if (betChecker == -1) {
							betChecker = pot.get(p.getName());
						} else {
							betCheck = false;
						}
					}
				}
				if (betCheck) {
					isBetDone = true;
					break;
				}
			}
			if (isBetDone) {
				break;
			}
		}
		if (onBet.size() == 1) {
			return 2;
		}
		if (isAllIn) {
			return 1;
		}
		return 0;
	}

	private void burn() {
		burn.add(dealerShoe.firstElement());
		dealerShoe.remove(0);
	}

	private void communityCardsDeal(int amount) {
		for (int i = 0; i < amount; i++) {
			communityCards.add(dealerShoe.firstElement());
			for (Player player : players) {
				player.giveCommunityCard(dealerShoe.firstElement());
			}
			dealerShoe.remove(0);
		}
	}

	private void match() {
		int winnerCombi = -1;
		int winnerHigh = -1;
		int winnerAddi = -1;
		int[][] winnerSet = { { 0 } };

		for (Player player : onBet) {
			int highCombi = 0;
			int combiHigh = 0;
			int additional = -1;

			int i = 0;
			int[][][] result = checkPlayer(communityCards, player.getHand());
			for (int[] combi : result[0]) {
				if (combi[0] > -1) {
					highCombi = i;
					combiHigh = combi[0];
					if (i == 2 || i == 5 || i == 6 || i == 8) {
						additional = combi[1];
					} else {
						additional = -1;
					}
				}
				i++;
			}

			System.out.print(player.getName() + "'s best:");
			for (int[] card : result[1]) {
				System.out.print("[" + patternP[card[0]] + " " + numberP[card[1]] + "]");
			}
			System.out.println();

			switch (highCombi) {
			case 0:
			case 1:
			case 3:
			case 4:
			case 7:
				System.out.println(player.getName() + ":" + numberP[combiHigh] + " " + combiP[highCombi]);
				break;
			case 2:
				System.out.println(player.getName() + ":" + numberP[combiHigh] + ", " + numberP[additional] + " "
						+ combiP[highCombi]);
				break;
			case 5:
				System.out.println(player.getName() + ":" + numberP[combiHigh] + " " + combiP[highCombi] + "("
						+ patternP[additional] + ")");
				break;
			case 6:
				System.out.println(player.getName() + ":" + combiP[highCombi] + "(" + numberP[combiHigh] + " tripple, "
						+ numberP[additional] + " pair)");
				break;
			case 8:
				System.out.println(
						player.getName() + ":" + patternP[additional] + numberP[combiHigh] + " " + combiP[highCombi]);
				break;
			}

			boolean change = false;
			if (winnerCombi < highCombi) {
				change = true;
			} else if (winnerCombi == highCombi) {
				if (winnerHigh < combiHigh) {
					change = true;
				} else if (winnerHigh == combiHigh) {
					if (winnerAddi < additional) {
						change = true;
					} else if (winnerAddi == additional) {
						int start = 0;
						switch (winnerCombi) {
						case 0:
							start = 1;
							break;
						case 1:
							start = 2;
							break;
						case 2:
							start = 4;
							break;
						case 3:
							start = 3;
							break;
						case 7:
							start = 4;
							break;
						}
						if (start > 0) {
							for (int s = start; s < 5; s++) {
								if (winnerSet[s][1] < result[1][s][1]) {
									change = true;
									break;
								} else if (winnerSet[s][1] > result[1][s][1]) {
									change = false;
									break;
								} else if (s == 4) {
									winner.add(player);
									break;
								}
							}
						} else {
							winner.add(player);
						}
					}
				}
			}

			if (change) {
				winner.clear();
				winner.add(player);
				winnerCombi = highCombi;
				winnerHigh = combiHigh;
				winnerAddi = additional;
				winnerSet = result[1];
			}
		}
		int valiGame = validateGame();
		Set<String> playerNames = pot.keySet();
		if (valiGame > 0) {
			if (valiGame == 1) {
				System.out.println("카드 개수가 맞지 않아 이번 게임을 무효화 합니다.");
			} else if (valiGame == 2) {
				System.out.println("중복된 카드가 검출되어 이번 게임을 무효화 합니다.");
			}

			for (String playerName : playerNames) {
				for (Player player : players) {
					if (player.getName().equals(playerName)) {
						player.giveChip(pot.get(playerName));
					}
				}
			}
		} else {
			int pot = 0;
			for (String playerName : playerNames) {
				pot += this.pot.get(playerName);
			}
			pot = pot - (pot % winner.size());
			System.out.print("Winner : ");
			for (Player player : winner) {
				System.out.print(player.getName() + " ");
			}
			System.out.println();
			for (Player player : winner) {
				int prize = pot / winner.size();
				player.giveChip(prize);
				chipCheck.replace(player.getName(), chipCheck.get(player.getName()) + prize);
			}
		}
	}

	private int[][][] checkPlayer(Vector<Vector<Character>> communityCards, Vector<Vector<Character>> hand) {
		int[] patternStat = { 0, 0, 0, 0 };
		int[] numberStat = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int[][] combiStat = { { -1 }, { -1 }, { -1, -1 }, { -1 }, { -1 }, { -1, -1 }, { -1, -1 }, { -1 }, { -1, -1 } };
		int[][] bestSet = { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
		boolean isFlush = false;
		boolean isStraight = false;
		int pairCount = 0;
		int tripsCount = 0;
		int fourOCount = 0;
		int top = -1;
		int pH = -1; // 페어 탑
		int ph = -1; // 페어 세컨드
		int tH = -1; // 트립스 탑
		int sH = -1; // 스트라이크 탑
		int flH = -1; // 플러시 탑
		int flP = -1; // 플러시 문양
		int fH = -1; // 포카드 탑

		// 카드 정렬하며 추가하기
		Vector<Vector<Character>> cards = new Vector<Vector<Character>>();
		for (Vector<Character> card : communityCards) {
			if (cards.isEmpty()) {
				cards.add(card);
			} else {
				for (int i = 0; i < cards.size(); i++) {
					if (numbers.indexOf(cards.get(i).get(1)) < numbers.indexOf(card.get(1))) {
						cards.add(i, card);
						break;
					} else if (numbers.indexOf(cards.get(i).get(1)) == numbers.indexOf(card.get(1))) {
						if (patterns.indexOf(cards.get(i).get(0)) < patterns.indexOf(card.get(0))) {
							cards.add(i, card);
							break;
						}
					}
					if (i == cards.size() - 1) {
						cards.add(card);
						break;
					}
				}
			}
		}
		for (Vector<Character> card : hand) {
			for (int i = 0; i < cards.size(); i++) {
				if (numbers.indexOf(cards.get(i).get(1)) < numbers.indexOf(card.get(1))) {
					cards.add(i, card);
					break;
				} else if (numbers.indexOf(cards.get(i).get(1)) == numbers.indexOf(card.get(1))) {
					if (patterns.indexOf(cards.get(i).get(0)) < patterns.indexOf(card.get(0))) {
						cards.add(i, card);
						break;
					}
				}
				if (i == cards.size() - 1) {
					cards.add(card);
					break;
				}
			}
		}

		for (Vector<Character> card : cards) {
			int i = 0;
			for (char pattern : patterns) {
				if (card.get(0) == pattern) {
					patternStat[i]++;
				}
				if (patternStat[i] >= 5) {
					flP = i;
					isFlush = true;
				}
				i++;
			}
			int j = 0;
			for (char number : numbers) {
				if (card.get(1) == number) {
					numberStat[j]++;
					if (top < j) {
						top = j;
					}
				}
				if (numberStat[j] > 1) {
					if (pH > -1) {
						ph = pH;
					}
					pH = j;
					if (numberStat[j] > 2) {
						tH = j;
						if (numberStat[j] > 3) {
							fH = j;
						}
					}
				}
				j++;
			}
		}

		// 플러시 판정
		if (isFlush) {
			for (Vector<Character> card : cards) {
				if (card.get(0) == patterns.get(flP)) {
					if (numbers.indexOf(card.get(1)) > flH) {
						flH = numbers.indexOf(card.get(1));
					}
				}
			}
		}

		// 스트레이트 및 페어 판정
		int straightCounter = 0;
		int i = 0;
		for (int count : numberStat) {
			if (count == 0) {
				straightCounter = 0;
			} else {
				straightCounter++;
				if (straightCounter >= 5) {
					sH = i;
				}
			}

			if (count == 2) {
				pairCount++;
			} else if (count == 3) {
				tripsCount++;
			} else if (count == 4) {
				fourOCount++;
			}
			i++;
		}
		if (straightCounter >= 5) {
			isStraight = true;
		}

		// 조합 판정
		if (isFlush) {
			if (isStraight) {
				// 스트레이트 플러시
				combiStat[8][0] = sH;
				combiStat[8][1] = flP;
			}
			// 플러시
			combiStat[5][0] = flH;
			combiStat[5][1] = flP;
		}
		if (fourOCount > 0) {
			// 포카드
			combiStat[7][0] = fH;
		}
		if (tripsCount > 0) {
			if (tripsCount == 2) {
				// 풀하우스(트리플 2개가 나온 경우)
				combiStat[6][0] = tH;
				if (tH == pH) {
					combiStat[6][1] = ph;
				} else {
					combiStat[6][1] = pH;
				}
			} else if (pairCount > 0) {
				// 풀하우스(트리플 1개 페어 1개 이상 나온 경우)
				combiStat[6][0] = tH;
				if (tH == pH) {
					combiStat[6][1] = ph;
				} else {
					combiStat[6][1] = pH;
				}
			} else {
				// 트립스
				combiStat[3][0] = tH;
			}
		}
		if (isStraight) {
			// 스트레이트
			combiStat[4][0] = sH;
		}
		if (pairCount > 0) {
			if (pairCount > 1) {
				// 투페어
				combiStat[2][0] = pH;
				combiStat[2][1] = ph;
			}
			// 원페어
			combiStat[1][0] = pH;
		}
		combiStat[0][0] = top;

		boolean done = false;
		for (int k = 8; k >= 0; k--) {
			if (combiStat[k][0] > -1) {
				int high;
				int pattern;
				Vector<Integer> selected = new Vector<Integer>();
				int n = 0;
				done = true;
				switch (k) {
				case 8:
					high = combiStat[8][0];
					pattern = combiStat[8][1];
					n = 0;
					for (int c = 0; c < 7; c++) {
						if (patterns.indexOf(cards.get(c).get(0)) == pattern) {
							if (numbers.indexOf(cards.get(c).get(1)) == high) {
								bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
								bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
								n++;
								high--;
								selected.add(0, c);
							}
						}
						if (n == 5) {
							break;
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 7:
					high = combiStat[7][0];
					n = 0;
					for (int c = 0; c < 7; c++) {
						if (numbers.indexOf(cards.get(c).get(1)) == high) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							selected.add(0, c);
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 6:
					int highT = combiStat[6][0];
					int highP = combiStat[6][1];
					n = 0;
					int m = 3;
					for (int c = 0; c < 7; c++) {
						if (numbers.indexOf(cards.get(c).get(1)) == highT) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							selected.add(0, c);
						} else if (numbers.indexOf(cards.get(c).get(1)) == highP) {
							bestSet[m][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[m][1] = numbers.indexOf(cards.get(c).get(1));
							m++;
							selected.add(0, c);
						}
						if (m == 5) {
							break;
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 5:
					pattern = combiStat[5][1];
					n = 0;
					for (int c = 0; c < 7; c++) {
						if (patterns.indexOf(cards.get(c).get(0)) == pattern) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							selected.add(0, c);
						}
						if (n == 5) {
							break;
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 4:
					high = combiStat[4][0];
					n = 0;
					for (int c = 0; c < 7; c++) {
						if (numbers.indexOf(cards.get(c).get(1)) == high) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							high--;
							selected.add(0, c);
						}
						if (n == 5) {
							break;
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 3:
					high = combiStat[3][0];
					n = 0;
					for (int c = 0; c < 7; c++) {
						if (numbers.indexOf(cards.get(c).get(1)) == high) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							selected.add(0, c);
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 2:
					high = combiStat[2][0];
					int subHigh = combiStat[2][1];
					n = 0;
					m = 2;
					for (int c = 0; c < 7; c++) {
						if (numbers.indexOf(cards.get(c).get(1)) == high) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							selected.add(0, c);
						} else if (numbers.indexOf(cards.get(c).get(1)) == subHigh) {
							bestSet[m][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[m][1] = numbers.indexOf(cards.get(c).get(1));
							m++;
							selected.add(0, c);
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				case 1:
					high = combiStat[1][0];
					n = 0;
					for (int c = 0; c < 7; c++) {
						if (numbers.indexOf(cards.get(c).get(1)) == high) {
							bestSet[n][0] = patterns.indexOf(cards.get(c).get(0));
							bestSet[n][1] = numbers.indexOf(cards.get(c).get(1));
							n++;
							selected.add(0, c);
						}
					}
					for (int select : selected) {
						cards.remove(select);
					}
					break;
				}
			}
			if (done) {
				for (int l = 0; l < cards.size() - 2; l++) {
					bestSet[l + (7 - cards.size())][0] = patterns.indexOf(cards.get(l).get(0));
					bestSet[l + (7 - cards.size())][1] = numbers.indexOf(cards.get(l).get(1));
				}
				break;
			}
		}

//      //제대로 처리되는지 확인용
//      for(Vector<Character> card: cards) {
//         System.out.println(card);
//      }
//      
//      System.out.print("Pattern : ");
//      for(int x : patternStat) {
//         System.out.print(x+" ");
//      }
//      System.out.println();
//      
//      System.out.print("Number : ");
//      for(int y : numberStat){
//         System.out.print(y+" ");
//      }
//      System.out.println();
//      
//      System.out.print("Combinations : ");
//      for(int [] k : combiStat) {
//         for(int t : k) {
//            System.out.print(t+" ");
//         }
//         System.out.print("/");
//      }
//      System.out.println();
//      
//      System.out.print("Best Set : ");
//      for(int [] k : bestSet) {
//         for(int t : k) {
//            System.out.print(t+" ");
//         }
//         System.out.print("/");
//      }
//      System.out.println();
//      
//      System.out.print("Best Set : ");
//      for(int [] k : bestSet) {
//         System.out.print(patterns.get(k[0])+" "+numbers.get(k[1])+"/");
//      }
//      System.out.println();

		int[][][] result = { { { 0 } }, { { 0 } } };
		result[0] = combiStat;
		result[1] = bestSet;
		return result;
	}

	private int validateGame() {
		dealerShoe.addAll(communityCards);
		dealerShoe.addAll(burn);
		for (Player player : players) {
			dealerShoe.addAll(player.getHand());
		}
		if (dealerShoe.size() != 52) {
			// 카드 수 오류
			return 1;
		}
		Vector<Vector<Character>> cards = new Vector<Vector<Character>>();
		for (Vector<Character> card : dealerShoe) {
			if (cards.contains(card)) {
				// 중복카드 검출
				return 2;
			}
			cards.add(card);
		}
		return 0;
	}

	private void validateChips() {
		for (Player player : players) {
			if (player.getChip() != chipCheck.get(player.getName())) {
				System.out.println(player.getName() + "의 칩 계산 오류 발견");
				System.out.println(player.getName() + "가 계산한 칩 수 : " + player.getChip());
				System.out.println("Dealer가 계산한 칩 수 : " + chipCheck.get(player.getName()));
				player.validateChip(chipCheck.get(player.getName()));
			}
		}
	}

	private void cleanTable() {
		dealerShoe.clear();
		communityCards.clear();
		burn.clear();
		winner.clear();
		pot.clear();
		onBet.clear();
	}

	private void viewSituation() {
		System.out.println("칩 현황");
		Set<String> playerNames = chipCheck.keySet();
		for (String playerName : playerNames) {
			System.out.println(playerName + ":" + chipCheck.get(playerName));
		}
		System.out.println("테이블 현황");
		for (Vector<Character> card : communityCards) {
			System.out.print(
					"[" + patternP[patterns.indexOf(card.get(0))] + " " + numberP[numbers.indexOf(card.get(1))] + "]");
		}
		System.out.println();
	}
}