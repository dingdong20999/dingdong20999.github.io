import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._


object StreamWordCount {
  def main(args: Array[String]): Unit = {
    //创建流处理的执行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //从程序运行参数中读取hostname和port
    val params: ParameterTool = ParameterTool.fromArgs(args)
    //    val hostname: String = params.get("host")
    //    val port: Int = params.getInt("port")

    //接收socket文本流
    val inputDataStream: DataStream[String] = env.socketTextStream("hadoop101", 7777)

    //定义转换操作wordcount
    val resultDataStream: DataStream[(String, Int)] = inputDataStream
      .flatMap(_.split(" "))
      .filter(_.nonEmpty)
      .map((_,1))
      .keyBy(0)
      .sum(1)

    resultDataStream.print()
    env.execute("steam word count job")

  }
}