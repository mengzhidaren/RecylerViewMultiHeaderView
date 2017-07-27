----
本库是基于RecyclerViewHeader的扩展。
###具有可增加RecyclerView头部View的 ViewGroup，支持与WebView，Video,View 嵌套使用

# 特性
1. 解决webView在RecyclerView中的滑动冲突和点击事件
2. 增加滑动RecyclerView中视频为小屏模式（功能参考美拍）
3. 优化过渡绘制卡的问题

#还未完善的问题
1. RecyclerView和webView之间切换时滑动完美  但两个之间的滑动惯性传递不是很完美
（欢迎留言惯性的优化方案）

### Demo
可以下载Demo查看。
[Download](https://github.com/mengzhidaren/RecylerViewMultiHeaderView/blob/master/apk/debug/app-debug.apk?raw=true)
# 截图
## HeaderVideo  HeaderView   
<image src="./img/111.gif" width="300px"/>  <image src="./img/222.gif" width="300px"/>  
## HeaderWebView
  <image src="./img/333.gif" width="300px"/>
  
## 引入
* Gradle
```groovy
compile 'com.yyl.multiview:recyclerview-multiheaderview:1.0.1'
```

## 开发
在xml中引用RecyclerViewMultiHeader：
```xml
    <com.yyl.multiview.RecyclerViewMultiHeader
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:viewState="video"
            app:videoScale="9/16">
....
        </com.yyl.multiview.RecyclerViewMultiHeader>
    
    
    
```

```java
// 设置视频监听。
    //视频小窗口开关
   public void setScreenSmallDisable(boolean stateVideoSmallDisable)
    //视频小窗口点击事件
    public void setScreenSmallOnClick(ScreenSmallOnClick smallOnClick)
    //视频小窗口监听动画事件
    public void setScreenChangeSmallCallBack(ScreenChangeSmallCallBack screenChangeSmall)
    
```

### 参考代码
* [RecyclerViewHeader](https://github.com/blipinsk/RecyclerViewHeader)

本库的是以RecyclerViewHeader为基础在功能上做的扩展，感谢作者开源库。


License
=======

    Copyright 2015 Bartosz Lipiński
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.