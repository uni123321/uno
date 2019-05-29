package myuno;

import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;


// 用户界面框架
public class unoframe extends JFrame {

	// 默认
	private static final long serialVersionUID = 1L;
	
	public static final String rules = "<html><body>游戏名：UNO小游戏<br>" + 
			"<br>" + 
			"作者：黄世悦、叶钊珲、王泽鑫<br>" + 
			"<br>" + 
			"仅供学习使用。图片、音乐素材来自网络。<br>" + 
			"<br>" + 
			"UNO牌型和规则说明：<br>" + 
			"108张牌：普通牌76张 = 4色 * 19张，每种颜色含1 * (0) + 2 * (1..9)；功能牌24张 = 3种 * 4色 * 2张，3种分别为skip、reverse、+2；万能牌8张 = 4张变色 + 4张(+4)。分数：万能牌为50，功能牌为20，其余按数字大小。<br>" + 
			"<br>" + 
			"游戏为4人一局。中途掉线视为退出游戏。<br>" + 
			"开局发牌，每人7张。首张分数最大者之中编号最小的为先手。翻下一张作为先手出牌的根据，若不是普通牌则重新翻牌。<br>" + 
			"按箭头指示的方位、颜色轮流出牌，没有抢出、连出，对子也只能分两轮出。和当前牌同色或同牌型的牌为可用（万能牌除外）。玩家摸牌当且仅当没有可用的牌。摸牌1张后，若这张牌可用，就自动打出，否则轮到下一家出牌。如果牌堆被摸完，就把除了玩家手中的牌和当前牌之外的牌重新放入牌堆。<br>" + 
			"skip跳过下一家，reverse反向（如果只有两人，就相当于skip），+2下一家摸2张且跳过。变色和+4需指定下一颜色。+4仅在无其他牌可用的情况下使用，变色牌无此限制。<br>" + 
			"最后一张可以是任意的牌。胜者为最先出完所有牌的玩家，或者唯一在线的玩家。没有点跳、连续罚牌等特殊规则。<br>" + 
			"20秒不出牌判定为超时掉线。<br>" +
			"<br>" + 
			"联机玩法：先建立服务器，输入65535以内的非负整数作为端口号。之后，各位玩家点击“联机模式”按钮，输入相应的IP地址和端口号，等待匹配。匹配满4人即可开始游戏。<br>" + 
			"<br>" + 
			"可按F5键暂停/播放背景音乐。祝游戏愉快！<body></html>";

	// 通信工具
	public frametel tel;
	public unoserver sv;
	public mymusic msc;
	
	// buttons & labels
	public JButton start;
	public JButton restart;
	public JLabel title1;
	public JLabel title2;
	public JLabel title3;
	public JButton start2;
	public JButton start3;
	public JButton help;
	
	public JButton ret;
	public JLabel help1;
	public JLabel help2;
	
	public JLabel winner;
	public JLabel loser;
	public JLabel lost;
	public JLabel waiting;
	public JLabel uno1;
	public JLabel uno2;
	
	private JPanel contentPane;			// 默认
	
	public JButton [] mycard;
	public JLabel [] othcard;
	public JLabel [] oppcard;
	public JLabel [] othnum;
	
	public JButton [] choosecolor;		// 变色牌选颜色按钮
	public JLabel info;					// 提示信息
	
	public JLabel nowcard;				// 当前的牌
	public JButton newcard;				// 摸牌按钮
	
	public JLabel [] turn;
	public JLabel dire;
	
	// 绘图常用数据
	private static final int windoww = 1000;
	private static final int windowh = 600;
	
	private static final int bod = 60;		// 边缘间距
	
	private static final int cardw = 60;
	private static final int cardh = 93;
	private static final int cardd = 5;		// 牌间距
	
	// mycard 位置
	private static final int cardy = windowh - bod - cardh;
	
	// othcard 位置
	private static final int [] backx = new int[] {bod, windoww - bod - cardw};
	private static final int backy = (windowh - cardh) / 2;
	
	// othnum 位置
	private static final int numw = 60;
	private static final int numh = 30;
	
	private static final int turnw = 36;
	private static final int turnh = 36;
	
	private static final int mpngw = 259;
	private static final int mpngh = 224;
	private static final int mlpngw = 370;
	private static final int mlpngh = 320;
	
	private static final ImageIcon back = new ImageIcon("unoimg\\back.png");
	private static final ImageIcon whopng = new ImageIcon("unoimg\\who.png");
	private static final ImageIcon [] mydire = {new ImageIcon("unoimg\\cs.png"), new ImageIcon("unoimg\\cn.png")};
	private static final ImageIcon mtitle = new ImageIcon("unoimg\\mtitle.png");
	private static final ImageIcon mwin = new ImageIcon("unoimg\\mwin.png");
	private static final ImageIcon mlose = new ImageIcon("unoimg\\mlose.png");
	private static final ImageIcon mlost = new ImageIcon("unoimg\\mlost.png");
	private static final ImageIcon mwait = new ImageIcon("unoimg\\mwait.png");
	private static final ImageIcon muno1 = new ImageIcon("unoimg\\muno1.png");
	private static final ImageIcon muno2 = new ImageIcon("unoimg\\muno2.png");
	
	private ImageIcon [] cardpic;
	
	private static final Color bgcolor = new Color(200, 230, 250);// 原来的绿色 144, 238, 144
	
	// 其它变量
	static final int tot = 108;			// 牌总数
	
	private int [] cardnum;				// 各方牌的数量，按编号
	private int [] mycardnum;
	public int mypl;					// 我的编号
	public int mydirec;
	public int precard;
	private boolean starting;
	public int clearuno;
	
	// 暂停函数
	public static void mysleep(int tim)
	{
		try {
			Thread.sleep(tim);
		} catch (InterruptedException e) {
			
		}
	}
	
	// 编号转方位
	public String posname(int pl)
	{
		switch((pl + 4 - mypl) % 4)
		{
		case 0:
			return "下";
		case 1:
			return "左";
		case 2:
			return "上";
		default:
			return "右";
		}
	}
	
	// 颜色编号转颜色
	public Color colorname(int cl)
	{
		switch(cl)
		{
		case 0:
			return Color.RED;
		case 1:
			return Color.YELLOW;
		case 2:
			return Color.BLUE;
		default:
			return Color.GREEN;
		}
	}
	
	// 刷新对面的牌
	public void opprefresh()
	{
		int oppnum = cardnum[(mypl + 2) % 4];
		int x = (windoww - oppnum * cardw - (oppnum - 1) * cardd) / 2;
		for (int i = 0; i < oppnum; i++)
		{
			oppcard[i].setBounds(x + i * (cardw + cardd), bod, cardw, cardh);
			if (!oppcard[i].isVisible())
				oppcard[i].setVisible(true);
		}
		for (int i = oppnum; (i < tot && oppcard[i].isVisible()); i++)
			oppcard[i].setVisible(false);
	}
	
	// 刷新我的牌，不含牌面设置
	public void myrefresh()
	{
		int x = (windoww - cardnum[mypl] * cardw - (cardnum[mypl] - 1) * cardd) / 2;
		for (int i = 0; i < cardnum[mypl]; i++)
		{
			mycard[i].setBounds(x + i * (cardw + cardd), cardy, cardw, cardh);
			if (!mycard[i].isVisible())
				mycard[i].setVisible(true);
		}
		for (int i = cardnum[mypl]; (i < tot && mycard[i].isVisible()); i++)
			mycard[i].setVisible(false);
	}
	
	// 刷新左右的牌
	public void othrefresh()
	{
		for (int i = 0; i < 2; i++)
		{
			int num = cardnum[(mypl + i * 2 + 1) % 4];
			
			othnum[i].setText(String.valueOf(num));
			if (num == 1)
				othnum[i].setText("");
			if (othnum[i].isVisible() && num == 0)
			{
				othnum[i].setVisible(false);
				othcard[i].setVisible(false);
			}
			if (!othnum[i].isVisible() && num > 0)
			{
				othnum[i].setVisible(true);
				othcard[i].setVisible(true);
			}
		}
	}
	
	// 接收提示信息
	public void rcvinfo(String inf)
	{
		info.setText(inf);
	}
	
	// 接收开局信息，设置我的编号
	public void rcvstart(int pl)
	{
		starting = true;
		clearinfo(0);
		waiting.setVisible(false);
		mypl = pl;
		mydirec = 0;
		precard = -1;
		dire.setIcon(mydire[0]);
		for (int i = 0; i < 4; i++)
			cardnum[i] = 0;
		drawcards();
	}
	
	// 接收摸牌信息（1张），刷新（谁，摸到了什么）
	public void rcvcard(int pl, int nam)
	{
		++cardnum[pl];
		if (pl == mypl)
		{
			if (!starting)
			{
				uno2.setVisible(true);
				uno1.setVisible(false);
				clearuno = 1;
			}
			if (cardnum[mypl] == 7)
				starting = false;
			mycard[cardnum[mypl] - 1].setIcon(cardpic[unocard.getidx(nam)]);
			mycardnum[cardnum[mypl] - 1] = nam;
			myrefresh();
		}
		else if (pl == (mypl + 2) % 4)
		{
			opprefresh();
		}
		else
		{
			othrefresh();
		}
		clearinfo(2);
	}
	
	// 接收当前玩家、牌、颜色信息
	public void rcvnow(int pl, int nam, int cl)
	{
		unocard u = new unocard(nam);
		clearinfo(3);
		// 设置当前的牌
		if (nam < 100)
			nowcard.setIcon(cardpic[u.getidx()]);
		else
			nowcard.setIcon(cardpic[nam - (nam % 4) - 100 + cl + 54]);
		if (!nowcard.isVisible())
			nowcard.setVisible(true);
		
		clearturn();
		turn[(pl + 4 - mypl) % 4].setVisible(true);
		
		if (precard != nam && u.tp == 11)
		{
			mydirec = 1 - mydirec;
			dire.setIcon(mydire[mydirec]);
		}
		if (!dire.isVisible())
		{
			dire.setVisible(true);
		}
		precard = nam;
	}
	
	// 接收出牌信息，刷新（谁，出了第几张牌）
	public void rcvresult(int pl, int num)
	{
		--cardnum[pl];
		if (pl == mypl)
		{
			int nu = cardnum[mypl];
			unocard uu = new unocard(mycardnum[num]);
			if (uu.cardname >= 100 || uu.tp == 12 || uu.tp == 10)
			{
				uno1.setVisible(true);
				uno2.setVisible(false);
				clearuno = 2;
			}
			for (int i = num + 1; i <= nu; i++)
			{
				mycard[i - 1].setIcon(mycard[i].getIcon());
				mycardnum[i - 1] = mycardnum[i];
			}
			myrefresh();
		}
		else if (pl == (mypl + 2) % 4)
		{
			opprefresh();
		}
		else
		{
			othrefresh();
		}
		if (cardnum[pl] == 1)
		{
			if (pl == mypl)
			{
				info.setText("UNO!");
				uno1.setVisible(true);
				uno2.setVisible(false);
				clearuno = 1;
			}
			else
			{
				info.setText(posname(pl) + "方玩家：UNO!");
				uno2.setVisible(true);
				uno1.setVisible(false);
				clearuno = 1;
			}
		}
		else
			clearinfo(1);
	}
	
	// 接收是否需要选择颜色的信息
	public void rcvneed2(boolean need)
	{
		for (int i = 0; i < 4; i++)
			choosecolor[i].setVisible(need);
		clearturn();
		if (need)
			info.setText("请选择颜色");
		else
			clearinfo(0);
	}
	
	// 接收玩家掉线信息（谁掉线了）
	public void rcvexit(int pl)
	{
		waiting.setVisible(false);
		if (pl == mypl)
		{
			clearinfo(0);
			info.setText("你失联了 -_-||");
			for (int i = 0; i < 4; i++)
				choosecolor[i].setVisible(false);
			clearturn();

			nowcard.setVisible(false);
			newcard.setVisible(false);
			dire.setVisible(false);
			for (int i = 0; i < tot; i++)
				mycard[i].setVisible(false);
			
			lost.setVisible(true);
			restart.setVisible(true);
		}
		else
			info.setText(posname(pl) + "方玩家退出了游戏。");
		if (pl >= 0)
		{
			cardnum[pl] = 0;
			if (pl == mypl)
			{
				myrefresh();
			}
			else if (pl == (mypl + 2) % 4)
			{
				opprefresh();
			}
			else
			{
				othrefresh();
			}
		}
	}
	
	// 接收胜者信息，显示游戏结果（谁赢了，最后一张牌是什么）
	public void rcvwinner(int pl, int nam)
	{
		nowcard.setIcon(cardpic[unocard.getidx(nam)]);
		if (!nowcard.isVisible())
			nowcard.setVisible(true);
		for (int i = 0; i < 4; i++)
			choosecolor[i].setVisible(false);
		clearturn();

		clearinfo(0);
		
		if (pl == mypl)
		{
			mysleep(1000);		

			nowcard.setVisible(false);
			newcard.setVisible(false);
			dire.setVisible(false);
			
			for (int i = 0; i < tot; i++)
				mycard[i].setVisible(false);
			info.setText("你赢了，恭喜 O(∩_∩)O");
			winner.setVisible(true);
		}
		else
		{
			mysleep(1500);		

			nowcard.setVisible(false);
			newcard.setVisible(false);
			dire.setVisible(false);
			
			for (int i = 0; i < tot; i++)
				mycard[i].setVisible(false);
			info.setText("你输了 (T_T) " + posname(pl) + "方玩家获胜。");			
			loser.setVisible(true);
		}
		restart.setVisible(true);
	}
	
	// 默认
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					unoframe frame = new unoframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// 启动 unocontroller.play 线程
	class play implements Runnable{
		unoframe u;
		play(unoframe un)
		{
			u = un;
		}
		public void run()
		{
			unocontroller.play(u);
		}
	}
	
	// 批量绘图
	/*
	public JButton start;
	public JButton restart;
	private JPanel contentPane;
	public JButton [] mycard;
	public JLabel [] othcard;
	public JLabel [] oppcard;
	public JLabel [] othnum;
	
	public JButton [] choosecolor;
	public JLabel info;
	
	public JLabel nowcard;
	public JButton newcard;
	public JLabel nowwho;
	*/
	
	void drawstart()
	{
		start.setVisible(true);
		start2.setVisible(true);
		start3.setVisible(true);
		help.setVisible(true);
		title1.setVisible(true);
		title2.setVisible(true);
		title3.setVisible(true);
	}
	
	void drawcards()
	{
		newcard.setVisible(true);
		info.setText("");
		info.setVisible(true);
	}
	
	void clearstart()
	{
		start.setVisible(false);
		start2.setVisible(false);
		start3.setVisible(false);
		help.setVisible(false);
		title1.setVisible(false);
		title2.setVisible(false);
		title3.setVisible(false);
		clearinfo(0);
	}
	
	// 清除牌局
	void clearcards()
	{
		int i;
		for (i = 0; i < tot; i++)
		{
			mycard[i].setVisible(false);
			oppcard[i].setVisible(false);
		}
		for (i = 0; i < 2; i++)
		{
			othcard[i].setVisible(false);
			othnum[i].setVisible(false);
		}
		nowcard.setVisible(false);
		newcard.setVisible(false);
		dire.setVisible(false);
		for (i = 0; i < 4; i++)
			choosecolor[i].setVisible(false);
		clearturn();
		restart.setVisible(false);
		clearinfo(0);
	}
	
	void clearinfo(int mod)
	{
		if (mod != 3)
			info.setText("");
		if (mod == 0 || ((mod == 1 || mod == 3) && clearuno <= 0))
		{
			uno1.setVisible(false);
			uno2.setVisible(false);
			clearuno = 0;
		}
		if (mod == 1 || mod == 3)
			clearuno--;
	}
	
	void clearturn()
	{
		for (int i = 0; i < 4; i++)
			turn[i].setVisible(false);
	}
	
	// 出牌和选颜色
	private class mybtnclick implements ActionListener
	{
		int index;		// 按钮编号
		int mod;		// 出牌 mod = 1; 选颜色 mod = 2
		public mybtnclick(int i, int md)
		{
			index = i;
			mod = md;
		}
		// 鼠标点击事件
		public void actionPerformed(ActionEvent arg0) {
			clearinfo(2);
			if (mod == 1)
				tel.setans(index);
			else if (mod == 2)
				tel.setans2(index);
		}
	}
	
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public unoframe() {
		
		msc = new mymusic();
		new Thread(msc).start();
		
		mysleep(2000);
		
		setBackground(bgcolor);
		setTitle("UNO小游戏");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 设置窗口位置
		int windowx = (Toolkit.getDefaultToolkit().getScreenSize().width - windoww) / 2;
		int windowy = (Toolkit.getDefaultToolkit().getScreenSize().height - windowh) / 2;
		if (windowy >= 40)
			windowy -= 20;
		
		setBounds(windowx, windowy, windoww, windowh);
		contentPane = new JPanel();
		contentPane.setBackground(bgcolor);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// 引用自己
		unoframe u = this;
		
		// 初始化buttons & labels
		title1 = new JLabel("UNO小游戏", JLabel.CENTER);
		title1.setFont(new Font("华文彩云", Font.PLAIN, 72));
		title1.setBounds(100, 30, 800, 200);
		contentPane.add(title1);
		
		title2 = new JLabel("作者：黄世悦、叶钊珲、王泽鑫");
		title2.setFont(new Font("楷体", Font.PLAIN, 24));
		title2.setBounds(580, 190, 450, 100);
		contentPane.add(title2);
		
		title3 = new JLabel("");		
		title3.setBounds(90, 190, mlpngw, mlpngh);
		title3.setIcon(mtitle);
		contentPane.add(title3);		
		
		start = new JButton("单机模式");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tel.canRun = false;
				clearstart();
				new Thread(new play(u)).start();
			}
		});
		start.setFont(new Font("宋体", Font.PLAIN, 18));
		start.setBounds(530, 340, 140, 60);
		contentPane.add(start);
		
		start2 = new JButton("联机模式");
		start2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearinfo(0);
				mypl = -1;
				if (tel.myconnect())
				{
					clearstart();
					rcvinfo("已连接，等待匹配...");
					waiting.setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "连接失败 -_-||", "错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		start2.setFont(new Font("宋体", Font.PLAIN, 18));
		start2.setBounds(710, 340, 140, 60);
		contentPane.add(start2);
		
		start3 = new JButton("新建服务器");
		start3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearinfo(0);
				if (sv == null)
				{
					try {
						String str = JOptionPane.showInputDialog("输入服务器 Port 端口");
						if (sv == null)
						{
							sv = new unoserver (Integer.parseInt(str));
							JOptionPane.showInputDialog("建立成功。端口：" + sv.ServerPort + "；IP如下：", sv.hostAddress);
						}
						else
							JOptionPane.showInputDialog("服务器已存在。端口：" + sv.ServerPort + "；IP如下：", sv.hostAddress);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "建立失败 -_-||", "错误", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
					JOptionPane.showInputDialog("服务器已存在。端口：" + sv.ServerPort + "；IP如下：", sv.hostAddress);
				
			}
		});
		start3.setFont(new Font("宋体", Font.PLAIN, 18));
		start3.setBounds(710, 420, 140, 60);
		contentPane.add(start3);
		
		help = new JButton("游戏说明");
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearstart();
				help1.setVisible(true);
				help2.setVisible(true);
				ret.setVisible(true);
			}
		});
		help.setFont(new Font("宋体", Font.PLAIN, 18));
		help.setBounds(530, 420, 140, 60);
		contentPane.add(help);
		
		newcard = new JButton("");
		newcard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tel.setans(-1);
			}
		});
		newcard.setIcon(back);
		newcard.setBorderPainted(false);
		newcard.setBackground(bgcolor);
		newcard.setBounds((windoww - bod)/2 - cardw, (windowh - cardh)/2, cardw, cardh);
		contentPane.add(newcard);
		newcard.setVisible(false);
		
		nowcard = new JLabel("");
		nowcard.setBounds((windoww + bod)/2, (windowh - cardh)/2, cardw, cardh);
		contentPane.add(nowcard);
		nowcard.setVisible(false);
		
		mycard = new JButton[tot];
		for (int i = 0; i < tot; i++)
		{
			mycard[i] = new JButton("");
			mycard[i].addActionListener(new mybtnclick(i, 1));
			mycard[i].setBounds(0, 0, cardw, cardh);
			mycard[i].setBorderPainted(false);
			mycard[i].setVisible(false);
			mycard[i].setBackground(bgcolor);
			contentPane.add(mycard[i]);
		}
		
		othcard = new JLabel[2];
		othnum = new JLabel[2];
		for (int i = 0; i < 2; i++)
		{
			othcard[i] = new JLabel("");
			othcard[i].setBounds(backx[i], backy, cardw, cardh);
			othcard[i].setIcon(back);
			othcard[i].setVisible(false);
			contentPane.add(othcard[i]);
			
			othnum[i] = new JLabel("", JLabel.CENTER);
			othnum[i].setFont(new Font("黑体", Font.BOLD, 28));
			othnum[i].setBounds(backx[i] + (cardw - numw) / 2, backy - cardd - numh, numw, numh);
			othnum[i].setVisible(false);
			contentPane.add(othnum[i]);
		}
		
		
		oppcard = new JLabel[tot];
		for (int i = 0; i < tot; i++)
		{
			oppcard[i] = new JLabel("");
			oppcard[i].setBounds(0, 0, cardw, cardh);
			oppcard[i].setIcon(back);
			oppcard[i].setVisible(false);
			contentPane.add(oppcard[i]);
		}
		
		choosecolor = new JButton[4];
		int chsclx = (windoww - numh * 4 - cardd * 3) / 2;
		for (int i = 0; i < 4; i++)
		{
			choosecolor[i] = new JButton("");
			choosecolor[i].addActionListener(new mybtnclick(i, 2));
			choosecolor[i].setBounds(chsclx + i * (numh + cardd), (windowh + cardh + bod)/2, numh, numh);
			choosecolor[i].setBackground(colorname(i));
			choosecolor[i].setVisible(false);
			contentPane.add(choosecolor[i]);
		}
		
		info = new JLabel("", JLabel.CENTER);
		info.setFont(new Font("黑体", Font.PLAIN, 24));
		info.setBounds(bod, bod - numh - cardd * 3, windoww - bod * 2, numh);
		info.setVisible(true);
		contentPane.add(info);
		
		restart = new JButton("返回标题");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearcards();
				winner.setVisible(false);
				loser.setVisible(false);
				lost.setVisible(false);
				drawstart();
			}
		});
		restart.setFont(new Font("宋体", Font.PLAIN, 24));
		restart.setBounds((windoww - 150) / 2, (windowh + cardh + bod)/2 + 70, 150, 50);
		restart.setVisible(false);
		contentPane.add(restart);
		
		help1 = new JLabel("游戏说明", JLabel.CENTER);
		help1.setFont(new Font("黑体", Font.PLAIN, 28));
		help1.setBounds(400, 20, 200, 40);
		help1.setVisible(false);
		contentPane.add(help1);
		
		help2 = new JLabel(rules);
		help2.setFont(new Font("宋体", Font.PLAIN, 16));
		help2.setBounds(50, 60, 900, 480);
		help2.setVisible(false);
		contentPane.add(help2);
		
		ret = new JButton("返回");
		ret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				help1.setVisible(false);
				help2.setVisible(false);
				ret.setVisible(false);
				drawstart();
			}
		});
		ret.setFont(new Font("宋体", Font.PLAIN, 18));
		ret.setBounds(860, 500, 80, 40);
		ret.setVisible(false);
		contentPane.add(ret);
		
		
		
		winner = new JLabel("");		
		winner.setBounds((windoww - mpngw) / 2, (windowh - mpngh) / 2, mpngw, mpngh);
		winner.setIcon(mwin);
		winner.setVisible(false);
		contentPane.add(winner);
		
		loser = new JLabel("");		
		loser.setBounds((windoww - mpngw) / 2, (windowh - mpngh) / 2, mpngw, mpngh);
		loser.setIcon(mlose);
		loser.setVisible(false);
		contentPane.add(loser);
		
		lost = new JLabel("");		
		lost.setBounds((windoww - mpngw) / 2, (windowh - mpngh) / 2, mpngw, mpngh);
		lost.setIcon(mlost);
		lost.setVisible(false);
		contentPane.add(lost);
		
		waiting = new JLabel("");		
		waiting.setBounds((windoww - mlpngw) / 2, (windowh - mlpngh) / 2 + 30, mlpngw, mlpngh);
		waiting.setIcon(mwait);
		waiting.setVisible(false);
		contentPane.add(waiting);
		
		uno1 = new JLabel("");		
		uno1.setBounds((windoww - mpngw) / 2 - 220, (windowh - mpngh) / 2, mpngw, mpngh);
		uno1.setIcon(muno1);
		uno1.setVisible(false);
		contentPane.add(uno1);
		
		uno2 = new JLabel("");		
		uno2.setBounds((windoww - mpngw) / 2 + 220, (windowh - mpngh) / 2, mpngw, mpngh);
		uno2.setIcon(muno2);
		uno2.setVisible(false);
		contentPane.add(uno2);
		
		
		turn = new JLabel[4];
		
		turn[0] = new JLabel("");
		turn[0].setBounds( (windoww - turnw)/2, cardy - (bod+turnh)/2, turnw, turnh);
		turn[0].setIcon(whopng);
		turn[0].setVisible(false);
		contentPane.add(turn[0]);
		
		turn[2] = new JLabel("");
		turn[2].setBounds( (windoww - turnw)/2, bod + cardh + (bod -turnh)/2, turnw, turnh);
		turn[2].setIcon(whopng);
		turn[2].setVisible(false);
		contentPane.add(turn[2]);
		
		turn[1] = new JLabel("");
		turn[1].setBounds( bod + cardw + (bod-turnw) / 2, (windowh-turnh)/2, turnw, turnh);
		turn[1].setIcon(whopng);
		turn[1].setVisible(false);
		contentPane.add(turn[1]);
		
		turn[3] = new JLabel("");
		turn[3].setBounds( windoww - bod - cardw - (bod+turnw)/2, (windowh-turnh)/2, turnw, turnh);
		turn[3].setIcon(whopng);
		turn[3].setVisible(false);
		contentPane.add(turn[3]);
		
		dire = new JLabel("", JLabel.CENTER);
		dire.setBounds((windoww - numh)/2, (windowh - numh) / 2, numh, numh);
		contentPane.add(dire);
		dire.setVisible(false);
		
		cardpic = new ImageIcon[62];
		for (int i = 0; i < 108; i ++)
		{
			int idx = unocard.getidx(i);
			if (cardpic[idx] == null)
				cardpic[idx] = new ImageIcon("unoimg\\"+unocard.filename(i)+".png");
		}
		String [] c = {"r", "y", "b", "g"};
		for (int i = 0; i < 4; i++)
		{
			cardpic[54 + i] = new ImageIcon("unoimg\\wild_"+c[i]+".png");
			cardpic[58 + i] = new ImageIcon("unoimg\\wild4_"+c[i]+".png");
		}
		

		// 初始化其它变量
		cardnum = new int[] {0, 0, 0, 0};
		mycardnum = new int [tot];
		mypl = 0;
		tel = new frametel(u);
		sv = null;
		mydirec = 0;
		precard = -1;
		starting = false;
		clearuno = 0;
		
		JRootPane rp = this.getRootPane(); 
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F5,0); 
		InputMap inputMap = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW); inputMap.put(stroke, KeyEvent.VK_F5); rp.getActionMap().put(KeyEvent.VK_F5, (Action) new AbstractAction() {
		    public void actionPerformed(ActionEvent e) { 
		    	msc.changestate();
		    } 
		});
	}
}
