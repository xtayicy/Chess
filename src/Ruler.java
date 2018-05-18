
public class Ruler {
	Chess[][] chess;
	boolean move = false;
	int i;
	int j;
	
	public Ruler(Chess[][] chess){
		this.chess = chess;
	}
	
	public boolean move(int startI,int startJ,int endI,int endJ,String name){
		int maxI;
		int maxJ;
		int minI;
		int minJ;
		move = true;
		
		if(startI > endI){
			maxI = startI;
			minI = endI;
		}else{
			maxI = endI; 
			minI = startI;
		}
		
		if(startJ > endJ){
			maxJ = startJ;
			minJ =endJ;
		}else{
			maxJ = endJ;
			minJ = startJ;
		}
		
		if(name.equals("车")){
			che(maxI,minI,maxJ,minJ);
		}else if(name.equals("马")){
			ma(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("相")){
			xiang1(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("士") || name.equals("相")){
			shi(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("帅")||name.equals("将")){
			jiang(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("炮")){
			pao(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("兵")){
			bing(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("象")){
			xiang2(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}else if(name.equals("卒")){
			zu(maxI,minI,maxJ,minJ,startI,startJ,endI,endJ);
		}
		
		return move;
	}

	private void zu(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		if(startJ > 4){
			if(startI != endI){
				move = false;
			}
			
			if(endJ - startJ != 1){
				move = false;
			}
		}else{
			if(startI == endI){
				if(endJ - startJ != 1){
					move = false;
				}
			}else if(startJ == endJ){
				if(maxI - minI != 1){
					move = false;
				}
			}else if(startI != endI && startJ != endJ){
				move = false;
			}
		}
	}

	private void xiang2(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI - minI;
		int b = maxJ - minJ;
		
		if(a == 2 && b == 2){
			if(endJ < 5){
				move = false;
			}
			
			if(chess[(maxI + minI)/2][(maxJ + minJ)/2] != null){
				move = false;
			}
		}else{
			move =false;
		}
	}

	private void bing(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		if(startJ < 5){
			if(startI != endI){
				move = false;
			}
			
			if(endJ - startJ != 1){
				move = false;
			}
		}else{
			if(startI == endI){
				if(endJ - startJ != 1){
					move = false;
				}
			}else if(startJ == endJ){
				if(maxI - minI != 1){
					move = false;
				}
			}else if(startI != endI && startJ != endJ){
				move = false;
			}
		}
	}

	private void pao(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		if(maxI == minI){
			if(chess[endI][endJ] != null){
				int count = 0;
				
				for(j = minJ + 1;j < maxJ;j++){
					if(chess[minI][j] != null){
						count++;
					}
				}
				
				if(count != 1){
					move = false;
				}
			}else if(chess[endI][endJ] == null){
				for(j = minJ + 1;j < maxJ;j++){
					if(chess[minI][j] != null){
						move =false;
						
						break;
					}
				}
			}
		}else if(maxJ == minJ){
			if(chess[endI][endJ] != null){
				int count = 0;
				
				for(i = minI + 1;i < maxI;i++){
					if(chess[i][minJ] != null){
						count++;
					}
				}
				
				if(count != 1){
					move = false;
				}
			}else if(chess[endI][endJ] == null){
				for(i = minI + 1;i < maxI;i++){
					if(chess[i][minI] != null){
						move =false;
						
						break;
					}
				}
			}
		}else if(maxJ != minJ && maxI != minI){
			move = false;
		}
	}

	private void jiang(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI - minI;
		int b = maxJ - minJ;
		
		if((a == 1 && b == 0) || (a == 0 && b ==1)){
			if(startJ > 4){
				if(endJ < 7){
					move = false;
				}
			}else{
				if(endJ > 2){
					move = false;
				}
			}
			
			if(endI > 5 || endI < 3){
				move = false;
			}
		}else{
			move = false;
		}
	}

	private void shi(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI -minI;
		int b = maxJ -minJ;
		
		if(a == 1 && b ==1){
			if(startJ > 4){
				if(endJ < 7 ){
					move = false;
				}
			}else{
				if(endJ > 2){
					move = false;
				}
			}
			
			if(endI > 5 || endI < 3){
				move = false;
			}
		}else{
			move = false;
		}
	}

	private void xiang1(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI - minI;
		int b = maxJ - minJ;
		
		if(a == 2 && b == 2){
			if(endJ > 4){
				move = false;
			}
			
			if(chess[(maxI + minI)/2][(maxJ + minJ)/2] != null){
				move = false;
			}
		}else{
			move =false;
		}
	}

	private void ma(int maxI, int minI, int maxJ, int minJ, int startI,
			int startJ, int endI, int endJ) {
		int a = maxI - minI;
		int b = maxJ - minJ;
		
		if(a == 1 && b == 2){
			if(startJ > endJ){
				if(chess[startI][startJ - 1] != null){
					move = false;
				}
			}else{
				if(chess[startI][startJ + 1] != null){
					move = false;
				}
			}
		}else if(a == 2 && b == 1){
			if(startI > endI){
				if(chess[startI - 1][startJ] != null){
					move = false;
				}
			}else{
				if(chess[startI + 1][startJ] != null){
					move = false;
				}
			}
		}else if(!((a == 2 && b == 1) || (a == 1 && b == 2))){
			move = false;
		}
	}

	private void che(int maxI, int minI, int maxJ, int minJ) {
		if(maxI == minI){
			for(j = minJ+1;j < maxJ;j++){
				if(chess[maxI][j] != null){
					move = false;
					
					break;
				}
			}
		}else if(maxJ == minJ){
			for(i = minJ + 1;i < maxJ;i++){
				if(chess[i][maxJ] != null){
					move = false;
					
					break;
				}
			}
		}else if(maxI != minI && maxJ != minJ){
			move = false;
		}
	}
}
