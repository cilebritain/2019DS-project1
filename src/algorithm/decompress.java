package algorithm;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class decompress {

    private static int t_num;
    private static int[] ls = new int[1001];
    private static int[] rs = new int[1001];

    public static void init(){
        t_num = 0;
        ls = new int[1001];
        rs = new int[1001];
    }

    public static int bytes2int(byte a, byte b, byte c, byte d){
        return (a & 0xFF)<<24 | (b & 0xFF)<<16 | (c & 0xFF)<<8 | (d & 0xFF);
    }

    public static void work(String input, String output) {
        init();
        try {
            RandomAccessFile file = new RandomAccessFile(input, "rw");
            RandomAccessFile ofile = new RandomAccessFile(output, "rw");
            FileChannel channel = file.getChannel();
            FileChannel ochanel = ofile.getChannel();
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                ByteBuffer obuffer = ByteBuffer.allocate(1024*1024*200);
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
                channel.close();
                ochanel.close();
            } catch (IOException e){
                System.out.println("IOException");
            }
        }catch (FileNotFoundException e){
            System.out.println("File not Found");
        }
    }
    public static void main(String[] args){
        String input = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file\\1.zip";
        String output = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file\\1test.jpg";
        work(input, output);
    }
}
