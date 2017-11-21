package com.xlh.crawler.utils;

        import java.io.FileReader;
        import javax.script.Invocable;
        import javax.script.ScriptEngine;
        import javax.script.ScriptEngineManager;
/**  * Java调用并执行js文件，传递参数，并活动返回值  *   * @author manjushri  */
public class ScriptEngineTest {
    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String jsFileName = "./expression.js";   // 读取js文件
        FileReader reader = new FileReader(jsFileName);   // 执行指定脚本
        engine.eval(reader);
        if(engine instanceof Invocable) {
            Invocable invoke = (Invocable)engine;    // 调用merge方法，并传入两个参数
// c = merge(2, 3);
            String c = (String)invoke.invokeFunction("userresponse", 189, "43ec517d68b6edd3015b3edc9a11367b19'");
            System.out.println("c = " + c);
        }
        reader.close();
    }
}