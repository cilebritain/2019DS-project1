package algorithm;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class decompress {

    private static int t_num;
    private static int[] ls = new int[1001];
    private static int[] rs = new int[1001];

    public static int bytes2int(byte a, byte b, byte c, byte d){
        //System.out.printf("%x %x %x %x\n",a,b,c,d);
        return (a & 0xFF)<<24 | (b & 0xFF)<<16 | (c & 0xFF)<<8 | (d & 0xFF);
    }

    public static String bytetobits(byte x){
        String ans="";
        for(int i=7;i>=0;i--){
            if(((x>>i)&1) ==1)ans+="1";
            else ans+="0";
        }
        return ans;
    }

    public static void dfs(int x,int p){
        if(x < 256){
            //System.out.printf("%d %d\n",x,p);
            return;
        }
        dfs(ls[x], p*2);
        dfs(rs[x], p*2+1);
    }

    public static void work(String input, String output) {
        try {
            RandomAccessFile file = new RandomAccessFile(input, "rw");
            RandomAccessFile ofile = new RandomAccessFile(output, "rw");
            FileChannel channel = file.getChannel();
            FileChannel ochanel = ofile.getChannel();
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                ByteBuffer obuffer = ByteBuffer.allocate(1024*1024*200);
//                int bbb = channel.read(buffer);
//                System.out.println(bbb);
//                bbb = channel.read(buffer);
//                System.out.println(bbb);
//                if(true)return;
                channel.read(buffer);
                buffer.flip();
                t_num = bytes2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
                buffer.clear();
                channel.read(buffer);
                buffer.flip();
                int root = bytes2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
                buffer.clear();
                channel.read(buffer);
                buffer.flip();
                int byte_num = bytes2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
                buffer.clear();
                buffer = ByteBuffer.allocate(12);
                for (int i = 1; i <= t_num; i++) {
                    channel.read(buffer);
                    buffer.flip();
                    int x = bytes2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
                    ls[x] = bytes2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
                    rs[x] = bytes2int(buffer.get(), buffer.get(), buffer.get(), buffer.get());
                    buffer.clear();
                }
                //dfs(root,0);

                int now = root, sum = 0;
                buffer = ByteBuffer.allocate(1024*1024*200);
                while (channel.read(buffer) > 0){
                    buffer.flip();
                    while(buffer.hasRemaining()){
                        byte p = buffer.get();
                        for(int i=7;i>=0;i--) {
                            if (((p >> i) & 1) == 1) now = rs[now];
                            else now = ls[now];
                            if(now < 256){
                                sum++;
                                if(sum <= byte_num) {
                                    obuffer.put((byte) (now));
                                }
                                now = root;
                            }
                            if(obuffer.position() == obuffer.capacity()){
                                obuffer.flip();
                                ochanel.write(obuffer);
                                obuffer.clear();
                                //System.out.println("fuck");
                            }
                        }
                    }
                    buffer.clear();
                }
                if(obuffer.position() > 0){
                    obuffer.flip();
                    ochanel.write(obuffer);
                    obuffer.clear();
                }
            } catch (IOException e){
                System.out.println("IOException");
            }
        }catch (FileNotFoundException e){
            System.out.println("File not Found");
        }
    }

    public static void debug() throws Exception{
        try {
            RandomAccessFile a = new RandomAccessFile("C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test1 - single file\\1.txt", "rw");
            RandomAccessFile b = new RandomAccessFile("C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test1 - single file\\1.txt.test.txt", "rw");
            FileChannel aa = a.getChannel();
            FileChannel bb = b.getChannel();
            ByteBuffer aaa = ByteBuffer.allocate(1);
            ByteBuffer bbb = ByteBuffer.allocate(1);
            int num = 0;
            while (true) {
                try {
                    aa.read(aaa);
                    bb.read(bbb);
                    aaa.flip();
                    bbb.flip();
                    byte x = aaa.get(), y = bbb.get();
                    aaa.clear();
                    bbb.clear();
                    if (x != y) {
                        System.out.printf("%d %s %s\n",num, bytetobits(x), bytetobits(y));
                        //return;
                    }
                    num++;
//                    if(num >= 1971){
//                        System.out.print(bytetobits(x));
//                        return;
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void test() throws Exception{
        RandomAccessFile a= new RandomAccessFile("a.txt", "rw");
        FileChannel channel= a.getChannel();
        byte[] b= new byte[]{
                (byte)(1),(byte)(1),(byte)(1),(byte)(1)
        };
        ByteBuffer buffer = ByteBuffer.wrap(b);
        //buffer.flip();
        buffer.get();
        buffer.get();
        buffer.get();
        buffer.get();
        buffer.flip();
        buffer.put((byte)1);
        channel.write(buffer);
        buffer.put((byte)1);
    }

    public static void main(String[] args) throws Exception{
        String input = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file\\1.zip";
        String output = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file\\1test.jpg";
        work(input, output);
//        debug();
        //test();
    }
}
