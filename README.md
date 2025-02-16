<p align="center">
  <img src="https://raw.githubusercontent.com/kitUIN/ModMultiVersionInterpreter/master/Icon.svg" width="350" height="220" alt="ModMultiVersionInterpreter"></a>
</p>
<div align="center">

# ModMultiVersionInterpreter

✨ 多版本代码同步-解析器 ✨

</div>
<p align="center">
  <a>
    <img src="https://img.shields.io/badge/license-MIT-green" alt="license">
  </a>
  <a >
    <img  src="https://img.shields.io/github/v/release/kitUIN/ModMultiVersionInterpreter" alt="release">
  </a>
</p>

## 实现功能
- `(` `)`
- `!` `&&` `||`
- `!=` `>` `>=` `<` `<=` `==`
- `&`识别为`&&`
- `|`识别为`||`
- `=`识别为`==` (定义模式中不生效)
- 左部省略自动补充`$$ ==`
- `$$`自动替换为具体内容
- 支持使用其他 关键字用于自动替换
- 支持`*-1.20.1`通配符识别加载器

## 使用
```kotlin
repositories {
    mavenCentral()
    maven {
        name = "kituinMavenReleases"
        url = uri("https://maven.kituin.fun/releases")
    }
}

dependencies {
    implementation("io.github.kituin:ModMultiVersionInterpreter:$interpreter_version")
}
```

## 布尔模式
```kotlin
val interpreter = Interpreter(">=fabric-1.20", mutableMapOf(
            "$$" to "fabric-1.20.3"
        ))
println(interpreter.interpret()) // true
```


## 相关项目
- [ModMultiVersion](https://github.com/kitUIN/ModMultiVersion) Idea插件-Minecraft模组多版本代码同步
- [ModMultiVersionTool](https://github.com/kitUIN/ModMultiVersionTool) ModMultiVersion-构建工具
- [ModMultiVersionInterpreter](https://github.com/kitUIN/ModMultiVersionInterpreter) ModMultiVersion自定义词法分析器,语法分析器,解释执行器