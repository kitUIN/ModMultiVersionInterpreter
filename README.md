## ModMultiVersion解析器
实现功能
- `(` `)`
- `!` `&&` `||`
- `!=` `>` `>=` `<` `<=` `==`
- `&`识别为`&&`
- `|`识别为`||`
- `=`识别为`==`
- 左部省略自动补充`$$ ==`
- `$$`自动替换为具体内容
- 支持使用其他 关键字用于自动替换
