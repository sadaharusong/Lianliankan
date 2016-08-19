package lianliankan.LLK;

import java.util.*;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class CtrlView extends GameView {
	LinkedList<Line> li;
	private RefreshHandler mRedrawHandler;

	public CtrlView(Context context, AttributeSet attr) {
		super(context, attr);
		mRedrawHandler = new RefreshHandler();
	}
	
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(process_value<=0){
				isLoose = true;
			}
			isLine = false;
			isWin = false;
			curX = lastX = 0;
			curY = lastY = 0;
			CtrlView.this.invalidate();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// �Ƴ���Ϣ�������������Ϣ���Ӷ���ȡ����Ϣ��
			sendMessageDelayed(obtainMessage(0), delayMillis);// ��ö�����Ϣ����ʱ����
		}
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println(keyCode);
		int i = 0;
		switch (keyCode) {
		//��
		case 19:
			do{
				i++;
				curY = curY == 0 ? ROW - 2 : curY - 1;
				if(i==ROW-1) {
					curX = curX== 0 ? COL - 1 : curX - 1;
					i = 0;
				}
			}while (map[curX][curY] == -1);
			break;
		//��
		case 20:
			do{
				i++;
				curY = curY == ROW -1 ? 1 : curY + 1;
				if(i==ROW-1) {
					curX = curX==COL - 2 ? 1 : curX + 1;
					i = 0;
				}
			}while (map[curX][curY] == -1);
			break;
		//��
		case 21:
			do{
				i++;
				curX = curX == 0 ? COL - 2 : curX - 1;
				if(i==COL-1) {
					curY = curY== 0 ? ROW -1 : curY - 1;
					i = 0;
				}
			}while (map[curX][curY] == -1);
			break;
		//��
		case 22:
			do{
				i++;
				curX = curX==COL -1 ? 1 : curX + 1;
				if(i==COL-1) {
					curY = curY==ROW -2 ? 1 : curY + 1;
					i = 0;
				}
			}while (map[curX][curY] == -1);
			break;
		//ȷ�� ������ε�����ϴε����λ�� p[0],p[1]
		case 23:
			lastX = curX;
			lastY = curY;
			isKeyDown = true;
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		while(isDead()){
			rearrange();
		}
		click();
		return true;
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		isKeyDown = false;
		//����ʱ����ⰴ��
		if(isLine) return false;
		//������ε�����ϴε����λ�� p[0],p[1]
		lastX = curX;
		lastY = curY;
		//�������ĵط�û��ͼƬ�����������鷶Χ���򱾴ε����Ч
		if((int) (event.getX() / width) >= ROW || (int) (event.getY() / height) >= COL ||
				map[(int) (event.getX() / width)][(int) (event.getY() / height)] == -1) return false;
			/*else if((int)event.getX() == lastX && (int)event.getY() == lastY)
			{
				return false;
			}*/
		else {
			curX = (int) (event.getX() / width);
			curY = (int) (event.getY() / height);
		}
		//��ͼ���������Զ���������
		while(isDead()){
			rearrange();
		}
		click();
		return true;
	}
	
	//��ʼ����ͼ��С
	public void initGame(){
		if(!isWin){
			count = DataSet.count;
			score = 0;	
			ROW = 6;
			COL = 6;
			
		}
		if (count >= 2) {
			ROW = 8;
			COL = 8;
		}
		process_value = 5*(200 - 25*(count-1)<=0?20:200 - 25*(count-1));
		isLoose = false;
		isLine = false;
		noticeCount = 0;
		randomIcons();
	}

	public class Point {
		public int x;
		public int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Point p) {
			if (p.x == x && p.y == y)
				return true;
			else
				return false;
		}
		
		public boolean isOut(){
			if(x<0 || x>ROW || y<0 || y>COL){
				return true;
			}
			return false;
		}
	}
	
	private void horizonLoad(int i, int j) {
		if (map[i][j] == -1) {
			if (i>=1 && i < ROW / 2) {
				map[i][j] = map[i - 1][j];
				map[i - 1][j] = -1;
				horizonLoad(i - 1, j);
			}else if (i >= ROW / 2 && i < ROW - 1) {
				map[i][j] = map[i + 1][j];
				map[i + 1][j] = -1;
				horizonLoad(i + 1, j);
			}
		}
	}
	
	private void verticalLoad(int i, int j) {
		if (map[i][j] == -1) {
			if (j>=1 && j < COL / 2) {
				map[i][j] = map[i][j - 1];
				map[i][j - 1] = -1;
				verticalLoad(i, j - 1);
			} else if(j >= COL/2 && j < COL - 1){
				map[i][j] = map[i][j + 1];
				map[i][j + 1] = -1;
				verticalLoad(i, j + 1);
			}
		}
	}

	private boolean horizon(Point a, Point b) {
		if (a.equals(b))// ����������ͬһ���ط���ֱ�ӷ���false
			return false;
		int x_start = a.y <= b.y ? a.y : b.y;
		int x_end = a.y <= b.y ? b.y : a.y;
		for (int x = x_start + 1; x < x_end; x++)
			// ֻҪһ������-1��ֱ�ӷ���false
			if (map[a.x][x] != -1) {
				return false;
			}
		// ���ػ�������
		p = new Point[] { a, b };
		lineType = H_LINE;
		return true;
	}

	private boolean vertical(Point a, Point b) {
		if (a.equals(b))// ����������ͬһ���ط���ֱ�ӷ���false
			return false;
		int y_start = a.x <= b.x ? a.x : b.x;
		int y_end = a.x <= b.x ? b.x : a.x;
		for (int y = y_start + 1; y < y_end; y++)
			if (map[y][a.y] != -1)
				return false;
		// ���ػ�������
		p = new Point[] { a, b };
		lineType = V_LINE;
		return true;
	}
	
	private boolean oneCorner(Point a, Point b) {
		Point c = new Point(a.x, b.y);
		Point d = new Point(b.x, a.y);
		if (map[c.x][c.y] == -1) {
			boolean method1 = horizon(a, c) && vertical(b, c);
			p = new Point[] { a, new Point(c.x, c.y), b };
			lineType = ONE_C_LINE;
			return method1;
		}
		if (map[d.x][d.y] == -1) {
			boolean method2 = vertical(a, d) && horizon(b, d);
			p = new Point[] { a, new Point(d.x, d.y), b };
			lineType = ONE_C_LINE;
			return method2;
		} else {
			return false;
		}
	}

	class Line {
		public Point a;
		public Point b;
		public int direct;

		public Line() {
		}

		public Line(int direct, Point a, Point b) {
			this.direct = direct;
			this.a = a;
			this.b = b;
		}
	}

	private LinkedList<Line> scan(Point a, Point b) {
		li = new LinkedList<Line>();
		for (int y = a.y; y >= 0; y--)
			if (map[a.x][y] == -1 && map[b.x][y] == -1
					&& vertical(new Point(a.x, y), new Point(b.x, y)))
				li.add(new Line(0, new Point(a.x, y), new Point(b.x, y)));

		for (int y = a.y; y < ROW; y++)
			if (map[a.x][y] == -1 && map[b.x][y] == -1
					&& vertical(new Point(a.x, y), new Point(b.x, y)))
				li.add(new Line(0, new Point(a.x, y), new Point(b.x, y)));

		for (int x = a.x; x >= 0; x--)
			if (map[x][a.y] == -1 && map[x][b.y] == -1
					&& horizon(new Point(x, a.y), new Point(x, b.y)))
				li.add(new Line(1, new Point(x, a.y), new Point(x, b.y)));

		for (int x = a.x; x < COL; x++)
			if (map[x][a.y] == -1 && map[x][b.y] == -1
					&& horizon(new Point(x, a.y), new Point(x, b.y)))
				li.add(new Line(1, new Point(x, a.y), new Point(x, b.y)));
		return li;
	}

	private boolean twoCorner(Point a, Point b) {
		li = scan(a, b);
		if (li.isEmpty())
			return false;
		for (int index = 0; index < li.size(); index++) {
			Line line = (Line) li.get(index);
			if (line.direct == 1) {
				if (vertical(a, line.a) && vertical(b, line.b)) {
					p = new Point[] { a, line.a, line.b, b };
					lineType = TWO_C_LINE;
					return true;
				}
			} else if (horizon(a, line.a) && horizon(b, line.b)) {
				p = new Point[] { a, line.a, line.b, b };
				lineType = TWO_C_LINE;
				return true;
			}
		}
		return false;
	}

	public boolean checkLink(Point a, Point b) {
		if (map[a.x][a.y] != map[b.x][b.y])// ���ͼ����ͬ��ֱ��Ϊfalse
			return false;
		if(map[a.x][a.y]==-1 && map[b.x][b.y]==-1){
			return false;
		}
		if (a.x == b.x && horizon(a, b)) // ����ͬһ������֮��û��ͼ������Ϊfalse
			return true;
		if (a.y == b.y && vertical(a, b)) // ����ͬһ������֮��û��ͼ������Ϊfalse
			return true;
		if (oneCorner(a, b))
			return true;
		else
			return twoCorner(a, b);
	}
	
	private void click(){
		invalidate();
		p = new Point[2];
		if (musicOn) {
			new Runnable() {
				@Override
				public void run() {
					clickPlayer.start();
				}
			}.run();
		}
		p[0] = new Point(lastX, lastY);
		p[1] = new Point(curX, curY);
		/**
		 * �������������ȥ�����ε����ͼƬ map[x][y]=-1ʱ����GameView�ϻ�icons
		 */

		if (checkLink(p[0], p[1])) {
			for (Point point : p) {
				// ѭ������p����û����������ȥ���ĵ�ͼƬ
				if (!point.isOut()) {
					if (map[point.x][point.y] != -1)
						map[point.x][point.y] = -1;
				}
			}
			if (musicOn) {
				new Runnable() {
					@Override
					public void run() {
						okPlayer.start();
					}
				}.run();
			}
			if (count == 3) {
				verticalLoad(p[0].x, p[0].y);
				verticalLoad(p[p.length - 1].x, p[p.length - 1].y);
				verticalLoad(p[0].x, p[0].y);
			}
			if (count == 4) {
				horizonLoad(p[0].x, p[0].y);
				horizonLoad(p[p.length - 1].x, p[p.length - 1].y);
				horizonLoad(p[0].x, p[0].y);
			}
			if (count > 4) {
				for(int i=0;i<ROW;i++){
					for(int j=0;j<COL;j++){
						horizonLoad(i, j);
						verticalLoad(i,j);
					}
				}
			}
			// û����һ��,�÷�Ϊ��ǰ��������ʮ,���ʱ���8�Ľ���
			score += 10 * count;
			process_value += 8;
			isLine = true;
			mRedrawHandler.sleep(500);
		} else
			isLine = false;
	}
	
	public boolean isDead(){
		List<Point> temp = new ArrayList<Point>();
		Point[] p =new Point[2];
		for (int i = 1; i < ROW-1; i++) {
			for (int j = 1; j < COL-1; j++) {
				if (map[i][j] != -1) {
					temp.add(new Point(i, j));
				}
			}
		}
		if(temp == null) return false;
		Iterator<Point> iterator1 = temp.iterator();
		int i=0,j=0;
		while(iterator1.hasNext()){
			p[0] = iterator1.next();
			//�Ż���ѯ
			/*Iterator<Point> iterator2 = list.iterator();
			while(iterator2.hasNext()){
				p[1] = iterator2.next();
				if(checkLink(p[0], p[1])){
					isLine = false;
					return false;
				}
			}*/
			//�Ż���ѯ�ռ临�Ӷ�
			for(i=j;i<temp.size();i++){
				p[1] = temp.get(i);
				if(checkLink(p[0], p[1])){
					isLine = false;
					return false;
				}
			}
			j++;
		}
		return true;
	}
}
