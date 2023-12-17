package com.rain.oj.questionservice.run;

import com.rain.oj.common.constant.CodeTemplateConstant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class LoadCodeTemplateToRedis implements CommandLineRunner {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        stringRedisTemplate.opsForValue().set(CodeTemplateConstant.CODE_TEMPLATE_KEY + "java", "import java.util.Scanner;\n" +
                "\n" +
                "// 注意类名必须为 Main, 不要有任何 package xxx 信息\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner in = new Scanner(System.in);\n" +
                "        // 注意 hasNext 和 hasNextLine 的区别\n" +
                "        while (in.hasNextInt()) { // 注意 while 处理多个 case\n" +
                "            int a = in.nextInt();\n" +
                "            int b = in.nextInt();\n" +
                "            System.out.println(a + b);\n" +
                "        }\n" +
                "    }\n" +
                "}");
        stringRedisTemplate.opsForValue().set(CodeTemplateConstant.CODE_TEMPLATE_KEY + "c", "#include <stdio.h>\n" +
                "\n" +
                "int main() {\n" +
                "    int a, b;\n" +
                "    while (scanf(\"%d %d\", &a, &b) != EOF) { // 注意 while 处理多个 case\n" +
                "        // 64 位输出请用 printf(\"%lld\") to\n" +
                "        printf(\"%d\\n\", a + b);\n" +
                "    }\n" +
                "    return 0;\n" +
                "}");
        stringRedisTemplate.opsForValue().set(CodeTemplateConstant.CODE_TEMPLATE_KEY + "cpp", "#include <iostream>\n" +
                "using namespace std;\n" +
                "\n" +
                "int main() {\n" +
                "    int a, b;\n" +
                "    while (cin >> a >> b) { // 注意 while 处理多个 case\n" +
                "        cout << a + b << endl;\n" +
                "    }\n" +
                "}\n" +
                "// 64 位输出请用 printf(\"%lld\")");
        stringRedisTemplate.opsForValue().set(CodeTemplateConstant.CODE_TEMPLATE_KEY + "python", "import sys\n" +
                "\n" +
                "for line in sys.stdin:\n" +
                "    a = line.split()\n" +
                "    print(int(a[0]) + int(a[1]))\n");
        stringRedisTemplate.opsForValue().set(CodeTemplateConstant.CODE_TEMPLATE_KEY + "go", "package main\n" +
                "\n" +
                "import (\n" +
                "    \"fmt\"\n" +
                ")\n" +
                "\n" +
                "func main() {\n" +
                "    a := 0\n" +
                "    b := 0\n" +
                "    for {\n" +
                "        n, _ := fmt.Scan(&a, &b)\n" +
                "        if n == 0 {\n" +
                "            break\n" +
                "        } else {\n" +
                "            fmt.Printf(\"%d\\n\", a + b)\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }
}
