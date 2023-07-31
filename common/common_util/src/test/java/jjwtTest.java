import com.tiktok.common_util.utils.JjwtUtil;

public class jjwtTest {
    public static void main(String[] args) {
        String token = JjwtUtil.createToken(123541L, "zhangsan");
        System.out.println("token = " + token);
        Long userId = JjwtUtil.getUserId(token);
        System.out.println("userId = " + userId);
    }
}
