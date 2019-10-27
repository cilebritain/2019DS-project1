package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class dir_decompress {

    private static String cache = "C:\\Users\\daiyuchun\\Desktop\\pj1\\cache1\\";

    public static void work(String input, String output){
        try {
            RandomAccessFile ifile = new RandomAccessFile(input, "rw");
            FileChannel ichannel = ifile.getChannel();
            ByteBuffer ibuffer = ByteBuffer.allocate(4);
            try {
                ichannel.read(ibuffer);
                ibuffer.flip();
                int num = decompress.bytes2int(ibuffer.get(), ibuffer.get(), ibuffer.get(), ibuffer.get());
                ibuffer.clear();
                int[] b_num = new int[num];
                String[] name = new String[num];
                for(int i=0; i<num; i++){
                    ichannel.read(ibuffer);
                    ibuffer.flip();
                    b_num[i] = decompress.bytes2int(ibuffer.get(), ibuffer.get(), ibuffer.get(), ibuffer.get());
                    ibuffer.clear();
                }
                for(int i=0; i<num; i++){
                    byte[] b = new byte[b_num[i]];
                    ibuffer = ByteBuffer.allocate(b_num[i]);
                    ichannel.read(ibuffer);
                    ibuffer.flip();
                    for(int j=0; j<b_num[i]; j++){
                        b[j] = ibuffer.get();
                    }
                    name[i] = new String(b);
                    ibuffer.clear();
                }
                for(int i=0; i<num; i++){
                    String[] p = name[i].split("\\\\");
                    String q = p[p.length-1];
                    if(q.indexOf(".")==-1){
                        File f = new File(output + name[i]);
                        f.mkdir();
                    }
                    else {
                        ibuffer = ByteBuffer.allocate(4);
                        ichannel.read(ibuffer);
                        ibuffer.flip();
                        int size = decompress.bytes2int(ibuffer.get(), ibuffer.get(), ibuffer.get(), ibuffer.get());
                        ibuffer.clear();

                        ibuffer = ByteBuffer.allocate(size);
                        String caname = name[i].replace("\\",".");
                        RandomAccessFile ca = new RandomAccessFile(cache+caname+".zip", "rw");
                        FileChannel ochannel = ca.getChannel();
                        ichannel.read(ibuffer);
                        ibuffer.flip();
                        ochannel.write(ibuffer);
                        ibuffer.clear();
                        ochannel.close();

                        decompress.work(cache+caname+".zip", output+name[i]);
                    }
                }
                File[] cacc = new File(cache).listFiles();
                for(File f: cacc){
                    f.delete();
                }
                ichannel.close();
            }catch (IOException e){
                System.out.println("IOException");
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public static void main(String[] args) {
        String input = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\test4 - large file.zip";
        String output = "C:\\Users\\daiyuchun\\Desktop\\pj1\\Test Cases\\";
        work(input, output);
    }
}
