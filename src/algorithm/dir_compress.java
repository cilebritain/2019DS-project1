package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class dir_compress {
    private static String cache = "C:\\Users\\daiyuchun\\Desktop\\pj1\\cache\\";

    public static void work(String input, String output){
        List<String> structure = new ArrayList<String>();
        List<String> zzip = new ArrayList<String>();
        Stack<String>stack = new Stack<String>();
        stack.push(input);
        while (!stack.empty()){
            String a = stack.pop();
            structure.add(a);
            File file = new File(a);
            if(file.isDirectory()){
                for(File f: file.listFiles()){
                    stack.add(f.getAbsolutePath());
                }
            }
        }
        String[] upd = input.split("\\\\");
        input = "";
        for (int i=0; i<upd.length-1; i++){
            input = input + upd[i] + "\\";
        }
        String pat = input.replace("\\","\\\\");
        for(String s:structure){
            File a = new File(s);
            if(!a.isDirectory()){
                String[] p = s.split(pat);
                String q = p[p.length-1];
                q = q.replace('\\','.');
                huffman.init();
                huffman.work(s,cache+q+".zip");
                zzip.add(cache+q+".zip");
            }
        }

        int num = structure.size(), b_sum=0;
        int[] b_num = new int[num];
        byte[] name = new byte[num*100];
        for(int i=0; i<num; i++){
            String[] p = structure.get(i).split(pat);
            String q = p[p.length-1];
            byte[] r = q.getBytes();
            b_num[i] = r.length;
            for(int j=0; j<r.length; j++)name[b_sum++] = r[j];
        }

        try{
            RandomAccessFile ofile = new RandomAccessFile(output, "rw");
            FileChannel ochanel = ofile.getChannel();
            try {
                ByteBuffer obuffer = ByteBuffer.allocate(4);
                obuffer.put(huffman.int2bytes(num));
                obuffer.flip();
                ochanel.write(obuffer);
                obuffer.clear();

                obuffer = ByteBuffer.allocate(4 * num);
                for (int i = 0; i < num; i++) {
                    obuffer.put(huffman.int2bytes(b_num[i]));
                }
                obuffer.flip();
                ochanel.write(obuffer);
                obuffer.clear();

                obuffer = ByteBuffer.wrap(name, 0, b_sum);
                ochanel.write(obuffer);
                obuffer.clear();

                obuffer = ByteBuffer.allocate(1024 * 1024 * 100);
                //File cac = new File(cache);
                for (String name_zip : zzip) {
                    RandomAccessFile gg = new RandomAccessFile(name_zip, "rw");
                    FileChannel ichannel = gg.getChannel();
                    ByteBuffer ibuffer = ByteBuffer.allocate(1024 * 1024 * 100);
                    int p = (int)gg.length();
                    obuffer.put(huffman.int2bytes(p));
                    try {
                        while (ichannel.read(ibuffer) != -1) {
                            ibuffer.flip();
                            while (ibuffer.hasRemaining()) {
                                obuffer.put(ibuffer.get());
                            }
                            ibuffer.clear();
                            obuffer.flip();
                            ochanel.write(obuffer);
                            obuffer.clear();
                        }
                    } catch (IOException e) {
                        System.out.println("IOException");
                    }
                    ichannel.close();
                }
//                File[] cacc = cac.listFiles();
//                for (File f: cacc){
//                    f.delete();
//                }
            }catch (IOException e){
                System.out.println("IOException");
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public static void main(String[] args) {
        String input = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file";
        String output = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file.zip";
        work(input, output);
    }
}
