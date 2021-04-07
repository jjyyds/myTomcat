import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;

public class HelloServlet extends HttpServlet {
    public HelloServlet(){
        System.out.println("HelloServlet构造");
    }

    @Override
    public void init() {
        System.out.println("HelloServlet init");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("HelloServlet doGet");
        String result="HTTP/1.1 200\r\nAccept-Ranges: bytes\r\nContent-Type: text/html;charset=UTF-8\r\nContent-Length: 0\r\n\r\n";
        try(OutputStream oos = resp.getOutputStream();){
            oos.write(result.getBytes());
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("HelloServlet doPost");
    }
}
