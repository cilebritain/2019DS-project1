package algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class huffman {
    private static int[] app = new int[1001];
    private static int t_num = 0, root = 0, hav_bytes=0;
    private static int[] h_info = new int[3001];
    private static int[] ls = new int[1001];
    private static int[] rs = new int[1001];
    private static int[][] code = new int[256][256];

    public static void init(){
        app = new int[1001];
        t_num = 0; root = 0; hav_bytes = 0;
        h_info = new int[3001];
        ls = new int[1001];
        rs = new int[1001];
        code = new int[256][256];
    }

    public static void dfs(int x, int p, int d){
        if(x < 256){
            code[x][0] = d;
            for(int i=1; i<=d; i++){
                code[x][i] = (p>>(d-i)) & 1; //从高位到低位
            }
            return;
        }
        t_num++;
        h_info[(t_num-1)*3 + 3] = x;
        h_info[(t_num-1)*3 + 4] = ls[x];
        h_info[(t_num-1)*3 + 5] = rs[x];
        dfs(ls[x], p*2, d+1);
        dfs(rs[x], p*2+1, d+1);
    }

    public static byte[] int2bytes(int x){
        byte[] ans = new byte[]{
                (byte)((x>>24) & 0xFF),
                (byte)((x>>16) & 0xFF),
                (byte)((x>>8) & 0xFF),
                (byte)((x) & 0xFF)
        };
        return ans;
    }

    public static void work(String input,String output){
        init();
        //key countting
        try {
            RandomAccessFile file = new RandomAccessFile(input, "rw");
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int p = 0;
            try {
                while (channel.read(buffer) != -1) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        p = buffer.get();
                        app[p & 0xFF] += 1;
                        hav_bytes++;
                    }
                    buffer.clear();
                }
            }catch (IOException e){
                System.out.println("IOException");
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not Find");
        }
        //huffman tree
        Comparator<Integer>comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return app[o1] - app[o2];
            }
        };
        Queue<Integer>heap = new PriorityQueue<Integer>(comparator);
        int now=256;
        for(int i=0; i<256; i++){
            if(app[i] > 0){
                heap.add(i);
            }
        }
        while (true){
            if(heap.size() == 0)break;
            if(heap.size() == 1){
                root = heap.poll();
                break;
            }
            Integer a = heap.poll();
            Integer b = heap.poll();
            app[now] = app[a] + app[b];
            ls[now] = a;
            rs[now] = b;
            heap.add(now);
            now++;
        }
        dfs(root,0,0);
        h_info[0] = t_num;
        h_info[1] = root;
        h_info[2] = hav_bytes;
        //compress
        int[] res = new int[300001];
        byte[] ret = new byte[50001];
        int[] rem = new int[10];
        int num_rem = 0;
        try {
            RandomAccessFile ifile = new RandomAccessFile(input, "rw");
            FileChannel ichannel = ifile.getChannel();
            ByteBuffer ibuffer = ByteBuffer.allocate(1024);
            RandomAccessFile ofile = new RandomAccessFile(output, "rw");
            FileChannel ochanel = ofile.getChannel();
            ByteBuffer obuffer;
            ByteBuffer hbuffer = ByteBuffer.allocate(4*(3 + t_num*3));
            for (int i = 0; i < 3 + 3 * t_num; i++) {
                hbuffer.put(int2bytes(h_info[i]));
            }
            try {
                hbuffer.flip();
                ochanel.write(hbuffer);
            }catch (IOException e){
                System.out.println("IOException");
            }
            try {
                while (ichannel.read(ibuffer) != -1){
                    int num=0, hav=0, pos=7, cnt=0;
                    ibuffer.flip();
                    while (ibuffer.hasRemaining()){
                        num = ibuffer.get() & 0xFF;
                        for(int i=1; i<=code[num][0]; i++){
                            res[hav++] = code[num][i]; //继续从高位到低位
                        }
                    }
                    ibuffer.clear();
                    ret[0] = 0;
                    for(int i=0;i < num_rem;i++){
                        ret[cnt] |= (rem[i]<<pos);
                        pos--;
                    }
                    for(int i=0; i < hav; i++){
                        if(pos==-1){
                            cnt++;
                            pos = 7;
                            ret[cnt] = 0;
                        }
                        ret[cnt] |= (res[i] << pos);
                        pos--;
                    }
                    num_rem = (hav+num_rem) % 8;
                    if(num_rem!=0){
                        ret[cnt]=0;
                        cnt--;
                        for(int i=num_rem; i>=1; i--){
                            rem[i-1] = res[hav-1-num_rem+i];
                        }
                    }
                    obuffer = ByteBuffer.wrap(ret, 0, cnt+1);
                    ochanel.write(obuffer);
                }
                if(num_rem > 0){
                    ret[0] = 0;
                    for(int i=0; i<num_rem; i++){
                        ret[0] |= (rem[i]<<(7-i));
                    }
                    obuffer = ByteBuffer.wrap(ret, 0, 1);
                    ochanel.write(obuffer);
                }
                ichannel.close();
                ochanel.close();
            }catch (IOException e){
                System.out.println("IOException");
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public static void main(String[] args){
        String input_file="C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file\\1.jpg";
        String output_dir="C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file\\1.zip";
        work(input_file, output_dir);
    }
}
