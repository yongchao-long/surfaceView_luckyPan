# surfaceView_luckyPan
今天给大家带来SurfaceView的一个实战案例，话说自定义View也是各种写，一直没有写过SurfaceView，这个玩意是什么东西？什么时候用比较好呢？

可以看到SurfaceView也是继承了View，但是我们并不需要去实现它的draw方法来绘制自己，为什么呢？

因为它和View有一个很大的区别，View在UI线程去更新自己；而SurfaceView则在一个子线程中去更新自己；这也显示出了它的优势，当制作游戏等需要不断刷新View时，因为是在子线程，避免了对UI线程的阻塞。

知道了优势以后，你会想那么不使用draw方法，哪来的canvas使用呢？

大家都记得更新View的时候draw方法提供了一个canvas，SurfaceView内部内嵌了一个专门用于绘制的Surface，而这个Surface中包含一个Canvas。

有了Canvas，我们如何获取呢？

SurfaceView里面有个getHolder方法，我们可以获取一个SurfaceHolder。通过SurfaceHolder可以监听SurfaceView的生命周期以及获取Canvas对象。
