----
本库是基于RecyclerViewHeader的扩展。
###RecyclerView头View的ViewGroup，支持与WebView，Video,View 嵌套使用

# 特性
1. 解决webView在RecyclerView中的滑动冲突和点击事件
2. 增加滑动RecyclerView中视频为小屏模式（功能参考美拍）
3. 优化过渡绘制卡的问题
4. (已解决)RecyclerView和webView之间切换时滑动完美,滑动惯性传递完美
5. 后期可能会对webView加一个滑动条有时间的话


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
compile 'com.yyl.multiview:recyclerview-multiheaderview:1.1.0'
```

## 开发
在xml中引用RecyclerViewMultiHeader：
```xml
    <com.yyl.multiview.RecyclerViewMultiHeader
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:viewState="video"  
            app:videoScale="0.5625">//video 9/16
....
        </com.yyl.multiview.RecyclerViewMultiHeader>
    
```

```
viewState
  <attr name="videoScale" format="float" />//显示比例
<attr name="viewState">
     <enum name="video" value="0" />//按videoScale比例 显示headView    在全屏后自动撑满全屏
     <enum name="view_header" value="1" />//以childView最大高度为最终高度
     <enum name="web" value="2" />//childView为全屏  其中setRequestFullWeb(false)会在webView内容不足一屏时 不填充整个VIEW
     <enum name="view_header_top" value="3" />//以最顶层childView高度为最终高度
</attr>
// 设置视频监听。
    //视频小窗口开关
   public void setScreenSmallDisable(boolean stateVideoSmallDisable)
    //视频小窗口监听
   public void setOnVideoSmallCallBack(OnVideoSmallCallBack onVideoSmallCallBack)

    //取消关联
    detach() 
```

### 参考代码
* [RecyclerViewHeader](https://github.com/blipinsk/RecyclerViewHeader)

本库的是以RecyclerViewHeader为基础在功能上做的扩展，感谢作者开源库。


## License
[MIT License](https://opensource.org/licenses/MIT).
