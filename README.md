# VanGogh
 simple MediaPicker like wechat （Kotlin + coroutines）
 
 download demoApp :https://www.pgyer.com/lzGS 
 


<div align=center><img width="280" height="500" src="https://user-images.githubusercontent.com/26602893/139173765-0bc711d2-7820-40a1-af54-9a59704561d6.jpg"/></div>
<div align=center><img width="280" height="500" src="https://user-images.githubusercontent.com/26602893/139173777-61c612d7-b80b-4b2d-830d-9d0d7f291192.jpg"/></div>

<div align=center><img width="280" height="500" src="https://user-images.githubusercontent.com/26602893/139175153-fd6d0c77-df27-4095-b528-e9955e756618.gif"/></div>


Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 
 Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ThirdPrince:VanGogh:1.0.6'
	}
 
Step 3. add 

     android:requestLegacyExternalStorage="true"  to your mainifest
     
 使用方法
 
 1 , 获取图片 （默认包含 gif ）

```
VanGogh.onlyImage().startForResult(this,
    onMediaResult = object : OnMediaResult {
        override fun onResult(mediaItemList: List<MediaItem>) {
         
        }

    })
```

	
	
 不包含 gif :

```
VanGogh.onlyImage(false).startForResult(this,
    onMediaResult = object : OnMediaResult {
        override fun onResult(mediaItemList: List<MediaItem>) {
          
        }

    })
```

2 , 使用头像

```
VanGogh.startForAvatarResult(this,onAvatarResult = object :
    OnAvatarResult {
    override fun onResult(image: MediaItem) {
     
    }

})
```
	
3 , 获取Media

```
VanGogh.getMedia().startForResult(this,
    onMediaResult = object : OnMediaResult {
        override fun onResult(mediaItemList: List<MediaItem>) {
         
        }

    })
```
	
getMedia 默认参数 包含 gif。

getMedia(false) 不包含 gif。
	
4 , 单独拍照

```
VanGogh.startForCameraResult(this,object :OnCameraResult{
    override fun onResult(image: MediaItem) {
       
    }

})
```

5 , 相册添加拍照选择

```
VanGogh.getMedia().enableCamera().startForResult(this,
    onMediaResult = object : OnMediaResult {
        override fun onResult(mediaItemList: List<MediaItem>) {
           
        }

    })
```
6, MediaItem 使用字段
	
图片默认是压缩的（gif 没有压缩）

建议使用 path 字段。

MeidiaItem 提供了判断是否是Video ,gif 的方法。
	
7 , 相册 选择 Media 个数

```
VanGogh.getMedia().setMaxMediaCount(9).startForResult(this,
    onMediaResult = object : OnMediaResult {
   
    }

})
```

下载体验地址：

https://www.pgyer.com/lzGS
	
