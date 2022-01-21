package com.company;

import java.util.ArrayList;
import java.util.Iterator;

public class Grid implements Iterable<iCell> {
    private iCell[][] board;
    private int rows, cols;

    public Grid(boolean[][] start, iCellFactory cf) {
        rows = start.length;
        cols = start[0].length;
        for (int r=0; r<rows; r++){
            for (int c=0; c<cols; c++){
                board[r][c] = cf.Make(this, r, c, start[r][c]);
            }
        }
    }

    public boolean[][] State(){
        boolean[][] state = new boolean[rows][cols];
        for (iCell c: this){
            state[c.row()][c.col()] = c.isAlive();
        }
        return state;
    }

    public iCell CellAt(int r, int c){
        return(r >= 0 && r < rows && c >= 0 && c < cols) ? board[r][c]:null;
    }

    public void OneGen() {
        //The commented code below was used to determine and update the state of the cell without threads
        //for (iCell cell: this) cell.determine();
        //for (iCell cell: this) cell.update();

        //the code below determines and udates the state of the cell using threading
        ArrayList<Thread> threads = new ArrayList<>();

        for(iCell cell: this) {
            MyThread run1 = new MyThread(cell);
            Thread thread1 = new Thread(run1);
            thread1.start();
            threads.add(thread1);
        }

        for(Thread myThread: threads) {
            try{
                myThread.join();
            }catch(InterruptedException e) {
                System.out.println("Fall");
            }
        }
    }

    public void Update(int Generations){
        for (int i=0; i<Generations; ++i){
            OneGen();
        }
    }

    public Iterator<iCell> iterator() {
        return new GridIterator();
    }

    class GridIterator implements Iterator<iCell> {
        private int r=0, c=0;
        public boolean hasNext() {
            return (r < rows) && (c < cols);
        }
        public iCell next() {
            if (!hasNext()) {
                return null;
            }
            iCell cell = board[r][c];
            c++;
            if (c >= cols) {
                c = 0;
                r++;
            }
            return cell;
        }
    }
}
