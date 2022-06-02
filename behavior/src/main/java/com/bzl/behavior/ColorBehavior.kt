package com.bzl.behavior

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.*
import com.bzl.behavior.ContextExt.randomColor

class ColorBehavior(val context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<AppCompatImageView>() {

    /**
     * Default constructor for inflating Behaviors from layout. The Behavior will have
     * the opportunity to parse specially defined layout parameters. These parameters will
     * appear on the child view tag.
     *
     * @param context
     * @param attrs
     */
    constructor() : this(null, null)

    /**
     * Called when the Behavior has been attached to a LayoutParams instance.
     * 当行为被附加到LayoutParams实例时调用。
     *
     * This will be called after the LayoutParams has been instantiated and can be
     * modified.
     *
     * @param params the LayoutParams instance that this Behavior has been attached to
     */
    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {}

    /**
     * Called when the Behavior has been detached from its holding LayoutParams instance.
     * 当行为已从其持有的LayoutParams实例分离时调用。
     *
     * This will only be called if the Behavior has been explicitly removed from the
     * LayoutParams instance via [LayoutParams.setBehavior]. It will not be
     * called if the associated view is removed from the CoordinatorLayout or similar.
     */
    override fun onDetachedFromLayoutParams() {}

    /**
     * Respond to CoordinatorLayout touch events before they are dispatched to child views.
     * 在CoordinatorLayout的触摸事件被分派给子视图之前响应它们。
     *
     * Behaviors can use this to monitor inbound touch events until one decides to
     * intercept the rest of the event stream to take an action on its associated child view.
     * This method will return false until it detects the proper intercept conditions, then
     * return true once those conditions have occurred.
     *
     * 翻译 ： behavior可以使用它来监视入站触摸事件，直到决定拦截事件流的其余部分，以便对其关联的子视图采取操作。 该方法将返回false，直到检测到适当的拦截条件，然后在这些条件发生后返回true。
     *
     *
     * Once a Behavior intercepts touch events, the rest of the event stream will
     * be sent to the [.onTouchEvent] method.
     *
     * 一旦行为拦截了触摸事件，事件流的其余部分将被发送到[.onTouchEvent] 方法.
     *
     * This method will be called regardless of the visibility of the associated child
     * of the behavior. If you only wish to handle touch events when the child is visible, you
     * should add a check to [View.isShown] on the given child.
     *
     * 无论行为的关联子元素是否可见，都会调用此方法。 如果你只希望在子对象可见时处理触摸事件，你应该在[View]中添加一个检查项。 在给定的子节点上显示。
     *
     *
     * The default implementation of this method always returns false.
     *
     * @param parent the parent view currently receiving this touch event
     * @param child the child view associated with this Behavior
     * @param ev the MotionEvent describing the touch event being processed
     * @return true if this Behavior would like to intercept and take over the event stream. 如果此行为想要拦截并接管事件流。
     * The default always returns false.
     */
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout, child: AppCompatImageView,
        ev: MotionEvent
    ): Boolean {
        return false
    }

    /**
     * Respond to CoordinatorLayout touch events after this Behavior has started
     * [intercepting][.onInterceptTouchEvent] them.
     *
     * 在此行为启动后响应CoordinatorLayout触摸事件[intercepting][.onInterceptTouchEvent] 它们.
     *
     *
     * Behaviors may intercept touch events in order to help the CoordinatorLayout
     * manipulate its child views. For example, a Behavior may allow a user to drag a
     * UI pane open or closed. This method should perform actual mutations of view
     * layout state.
     *
     * 行为可以拦截触摸事件，以帮助CoordinatorLayout操作它的子视图。 例如，一个Behavior可以允许用户打开或关闭一个UI窗格。 这个方法应该执行视图布局状态的实际变化。
     *
     *
     * This method will be called regardless of the visibility of the associated child
     * of the behavior. If you only wish to handle touch events when the child is visible, you
     * should add a check to [View.isShown] on the given child.
     *
     * @param parent the parent view currently receiving this touch event
     * @param child the child view associated with this Behavior
     * @param ev the MotionEvent describing the touch event being processed
     * @return true if this Behavior handled this touch event and would like to continue
     * receiving events in this stream. The default always returns false.
     */
    override fun onTouchEvent(
        parent: CoordinatorLayout, child: AppCompatImageView,
        ev: MotionEvent
    ): Boolean {
        return false
    }

    /**
     * TODO  设置背景色
     *
     * 需要配合 getScrimOpacity() 使用 因为 getScrimOpacity() 默认 = 0f
     */
    @ColorInt
    override fun getScrimColor(parent: CoordinatorLayout, child: AppCompatImageView): Int {
        return Color.BLACK
    }

    /**
     * Determine the current opacity of the scrim behind a given child view
     * 确定给定子视图后面的纱布的当前不透明度
     *
     * A scrim may be used to indicate that the other elements beneath it are not currently
     * interactive or actionable, drawing user focus and attention to the views above the scrim.
     *
     * scrim 可以用来表明下面的其他元素目前不是交互式的或可操作的，将用户的注意力吸引到 scrim上面的视图上。
     *
     * The default implementation returns 0.0f.
     *
     * @param parent the parent view of the given child
     * @param child the child view above the scrim
     * @return the desired scrim opacity from 0.0f to 1.0f. The default return value is 0.0f.
     */
    @FloatRange(from = 0.0, to = 1.0)
    override fun getScrimOpacity(parent: CoordinatorLayout, child: AppCompatImageView): Float {
        return 0f
    }

    /**
     * Determine whether interaction with views behind the given child in the child order
     * should be blocked.
     *  确定是否应该阻止与给定子进程后面的视图的交互。
     *
     * The default implementation returns true if
     * [.getScrimOpacity] would return > 0.0f.
     *
     * @param parent the parent view of the given child
     * @param child the child view to test
     * @return true if [.getScrimOpacity] would
     * return > 0.0f.
     */
    override fun blocksInteractionBelow(
        parent: CoordinatorLayout,
        child: AppCompatImageView
    ): Boolean {
        return getScrimOpacity(parent, child) > 0f
    }

    /**
     * Determine whether the supplied child view has another specific sibling view as a
     * layout dependency.
     * 确定提供的子视图是否有另一个特定的兄弟视图作为布局依赖项。
     *
     * This method will be called at least once in response to a layout request. If it
     * returns true for a given child and dependency view pair, the parent CoordinatorLayout
     * will:
     *
     *  1. Always lay out this child after the dependent child is laid out, regardless
     * of child order.
     *  1. Call [.onDependentViewChanged] when the dependency view's layout or
     * position changes.
     *
     * 此方法将至少被调用一次以响应布局请求。如果它为给定的子视图和依赖视图对返回true，父视图CoordinatorLayout将:
     * 1。无论子序列如何，都要在从属子序列被布局之后再布局这个子序列。
     * 1。(打电话。当依赖视图的布局或位置发生变化时。
     *
     * @param parent the parent view of the given child
     * @param child the child view to test
     * @param dependency the proposed dependency of child
     * @return true if child's layout depends on the proposed dependency's layout,
     * false otherwise
     *
     * @see .onDependentViewChanged
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout, child: AppCompatImageView,
        dependency: View
    ): Boolean {
        return dependency is MoveView
    }

    /**
     * Respond to a change in a child's dependent view
     * 对孩子依赖观念的改变做出反应
     *
     * This method is called whenever a dependent view changes in size or position outside
     * of the standard layout flow. A Behavior may use this method to appropriately update
     * the child view in response.
     *
     * 只要依赖视图的大小或位置在标准布局流之外发生变化，就会调用此方法。 行为可以使用此方法来适当地更新子视图作为响应。
     *
     *
     * A view's dependency is determined by [.layoutDependsOn] or if `child` has set another view as it's anchor.
     * 视图的依赖关系由 [.layoutDependsOn] 或 `child` 是否将另一个视图设置为锚点确定。
     *
     *
     * Note that if a Behavior changes the layout of a child via this method, it should
     * also be able to reconstruct the correct position in
     * [onLayoutChild][.onLayoutChild].
     * `onDependentViewChanged` will not be called during normal layout since
     * the layout of each child view will always happen in dependency order.
     *
     * 请注意，如果一个 Behavior 通过此方法更改子项的布局，它还应该能够在 [onLayoutChild][.onLayoutChild] 中重建正确的位置。
     * `onDependentViewChanged` 在正常布局期间不会被调用，因为每个子视图的布局总是以依赖顺序发生。
     *
     *
     * If the Behavior changes the child view's size or position, it should return true.
     * The default implementation returns false.
     * 如果 Behavior 改变了子视图的大小或位置，它应该返回 true。
     * 默认实现返回 false。
     *
     * @param parent the parent view of the given child
     * @param child the child view to manipulate
     * @param dependency the dependent view that changed
     * @return true if the Behavior changed the child view's size or position, false otherwise
     */
    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: AppCompatImageView,
        dependency: View
    ): Boolean {
        context?.let { child.setBackgroundColor(it.randomColor()) }
        return false
    }

    /**
     * Respond to a child's dependent view being removed.
     * 回应孩子的从属视图被删除。
     *
     * This method is called after a dependent view has been removed from the parent.
     * A Behavior may use this method to appropriately update the child view in response.
     * 在从父视图中移除依赖视图后调用此方法。
     * 行为可以使用此方法来适当地更新子视图作为响应。
     *
     * A view's dependency is determined by
     * [.layoutDependsOn] or
     * if `child` has set another view as it's anchor.
     *
     * @param parent the parent view of the given child
     * @param child the child view to manipulate
     * @param dependency the dependent view that has been removed
     */
    override fun onDependentViewRemoved(
        parent: CoordinatorLayout, child: AppCompatImageView,
        dependency: View
    ) {
    }

    /**
     * Called when the parent CoordinatorLayout is about to measure the given child view.
     * 当父 CoordinatorLayout 即将测量给定的子视图时调用。
     *
     * This method can be used to perform custom or modified measurement of a child view
     * in place of the default child measurement behavior. The Behavior's implementation
     * can delegate to the standard CoordinatorLayout measurement behavior by calling
     * [ parent.onMeasureChild][CoordinatorLayout.onMeasureChild].
     *
     * @param parent the parent CoordinatorLayout
     * @param child the child to measure
     * @param parentWidthMeasureSpec the width requirements for this view
     * @param widthUsed extra space that has been used up by the parent
     * horizontally (possibly by other children of the parent)
     * @param parentHeightMeasureSpec the height requirements for this view
     * @param heightUsed extra space that has been used up by the parent
     * vertically (possibly by other children of the parent)
     * @return true if the Behavior measured the child view, false if the CoordinatorLayout
     * should perform its default measurement
     */
    override fun onMeasureChild(
        parent: CoordinatorLayout, child: AppCompatImageView,
        parentWidthMeasureSpec: Int, widthUsed: Int,
        parentHeightMeasureSpec: Int, heightUsed: Int
    ): Boolean {
        return false
    }

    /**
     * Called when the parent CoordinatorLayout is about the lay out the given child view.
     *
     *
     * This method can be used to perform custom or modified layout of a child view
     * in place of the default child layout behavior. The Behavior's implementation can
     * delegate to the standard CoordinatorLayout measurement behavior by calling
     * [ parent.onLayoutChild][CoordinatorLayout.onLayoutChild].
     *
     *
     * If a Behavior implements
     * [.onDependentViewChanged]
     * to change the position of a view in response to a dependent view changing, it
     * should also implement `onLayoutChild` in such a way that respects those
     * dependent views. `onLayoutChild` will always be called for a dependent view
     * *after* its dependency has been laid out.
     *
     * @param parent the parent CoordinatorLayout
     * @param child child view to lay out
     * @param layoutDirection the resolved layout direction for the CoordinatorLayout, such as
     * [ViewCompat.LAYOUT_DIRECTION_LTR] or
     * [ViewCompat.LAYOUT_DIRECTION_RTL].
     * @return true if the Behavior performed layout of the child view, false to request
     * default layout behavior
     */
    override fun onLayoutChild(
        parent: CoordinatorLayout, child: AppCompatImageView,
        layoutDirection: Int
    ): Boolean {
        return false
    }

    // Utility methods for accessing child-specific, behavior-modifiable properties.

    @Deprecated(
        """You should now override
          {@link #onStartNestedScroll(CoordinatorLayout, View, View, View, int, int)}. This
          method will still continue to be called if the type is {@link ViewCompat#TYPE_TOUCH}.""",
        ReplaceWith("false")
    )
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, directTargetChild: View, target: View,
        @ViewCompat.ScrollAxis axes: Int
    ): Boolean {
        return false
    }

    /**
     * Called when a descendant of the CoordinatorLayout attempts to initiate a nested scroll.
     *
     *
     * Any Behavior associated with any direct child of the CoordinatorLayout may respond
     * to this event and return true to indicate that the CoordinatorLayout should act as
     * a nested scrolling parent for this scroll. Only Behaviors that return true from
     * this method will receive subsequent nested scroll events.
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param directTargetChild the child view of the CoordinatorLayout that either is or
     * contains the target of the nested scroll operation
     * @param target the descendant view of the CoordinatorLayout initiating the nested scroll
     * @param axes the axes that this nested scroll applies to. See
     * [ViewCompat.SCROLL_AXIS_HORIZONTAL],
     * [ViewCompat.SCROLL_AXIS_VERTICAL]
     * @param type the type of input which cause this scroll event
     * @return true if the Behavior wishes to accept this nested scroll
     *
     * @see NestedScrollingParent2.onStartNestedScroll
     */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, directTargetChild: View, target: View,
        @ViewCompat.ScrollAxis axes: Int, @ViewCompat.NestedScrollType type: Int
    ): Boolean {
        return if (type == ViewCompat.TYPE_TOUCH) {
            onStartNestedScroll(
                coordinatorLayout, child, directTargetChild,
                target, axes
            )
        } else false
    }


    @Deprecated(
        """You should now override
          {@link #onNestedScrollAccepted(CoordinatorLayout, View, View, View, int, int)}. This
          method will still continue to be called if the type is {@link ViewCompat#TYPE_TOUCH}."""
    )
    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, directTargetChild: View, target: View,
        @ViewCompat.ScrollAxis axes: Int
    ) {
        // Do nothing
    }

    /**
     * Called when a nested scroll has been accepted by the CoordinatorLayout.
     *
     *
     * Any Behavior associated with any direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of [.onStartNestedScroll]. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     *
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param directTargetChild the child view of the CoordinatorLayout that either is or
     * contains the target of the nested scroll operation
     * @param target the descendant view of the CoordinatorLayout initiating the nested scroll
     * @param axes the axes that this nested scroll applies to. See
     * [ViewCompat.SCROLL_AXIS_HORIZONTAL],
     * [ViewCompat.SCROLL_AXIS_VERTICAL]
     * @param type the type of input which cause this scroll event
     *
     * @see NestedScrollingParent2.onNestedScrollAccepted
     */
    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, directTargetChild: View, target: View,
        @ViewCompat.ScrollAxis axes: Int, @ViewCompat.NestedScrollType type: Int
    ) {
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedScrollAccepted(
                coordinatorLayout, child, directTargetChild,
                target, axes
            )
        }
    }


    @Deprecated(
        """You should now override
          {@link #onStopNestedScroll(CoordinatorLayout, View, View, int)}. This method will still
          continue to be called if the type is {@link ViewCompat#TYPE_TOUCH}."""
    )
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, target: View
    ) {
        // Do nothing
    }

    /**
     * Called when a nested scroll has ended.
     *
     *
     * Any Behavior associated with any direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of [.onStartNestedScroll]. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     *
     *
     *
     * `onStopNestedScroll` marks the end of a single nested scroll event
     * sequence. This is a good place to clean up any state related to the nested scroll.
     *
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param target the descendant view of the CoordinatorLayout that initiated
     * the nested scroll
     * @param type the type of input which cause this scroll event
     *
     * @see NestedScrollingParent2.onStopNestedScroll
     */
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, target: View, @ViewCompat.NestedScrollType type: Int
    ) {
        if (type == ViewCompat.TYPE_TOUCH) {
            onStopNestedScroll(coordinatorLayout, child, target)
        }
    }


    @Deprecated(
        """You should now override
          {@link #onNestedScroll(CoordinatorLayout, View, View, int, int, int, int, int, int[])}.
          This method will still continue to be called if neither
          {@link #onNestedScroll(CoordinatorLayout, View, View, int, int, int, int, int, int[])}
          nor {@link #onNestedScroll(View, int, int, int, int, int)} are overridden and the type is
          {@link ViewCompat#TYPE_TOUCH}."""
    )
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: AppCompatImageView,
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        // Do nothing
    }


    @Deprecated(
        """You should now override
          {@link #onNestedScroll(CoordinatorLayout, View, View, int, int, int, int, int, int[])}.
          This method will still continue to be called if
          {@link #onNestedScroll(CoordinatorLayout, View, View, int, int, int, int, int, int[])}
          is not overridden."""
    )
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: AppCompatImageView,
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, @ViewCompat.NestedScrollType type: Int
    ) {
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedScroll(
                coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed
            )
        }
    }

    /**
     * Called when a nested scroll in progress has updated and the target has scrolled or
     * attempted to scroll.
     *
     *
     * Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of [.onStartNestedScroll]. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     *
     *
     *
     * `onNestedScroll` is called each time the nested scroll is updated by the
     * nested scrolling child, with both consumed and unconsumed components of the scroll
     * supplied in pixels. *Each Behavior responding to the nested scroll will receive the
     * same values.*
     *
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     * @param dxConsumed horizontal pixels consumed by the target's own scrolling operation
     * @param dyConsumed vertical pixels consumed by the target's own scrolling operation
     * @param dxUnconsumed horizontal pixels not consumed by the target's own scrolling
     * operation, but requested by the user
     * @param dyUnconsumed vertical pixels not consumed by the target's own scrolling operation,
     * but requested by the user
     * @param type the type of input which cause this scroll event
     * @param consumed output. Upon this method returning, should contain the scroll
     * distances consumed by this Behavior
     *
     * @see NestedScrollingParent3.onNestedScroll
     */
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: AppCompatImageView,
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, @ViewCompat.NestedScrollType type: Int, consumed: IntArray
    ) {
        // In the case that this nested scrolling v3 version is not implemented, we call the v2
        // version in case the v2 version is. We Also consume all of the unconsumed scroll
        // distances.
        consumed[0] += dxUnconsumed
        consumed[1] += dyUnconsumed
        onNestedScroll(
            coordinatorLayout, child, target, dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed, type
        )
    }


    @Deprecated(
        """You should now override
          {@link #onNestedPreScroll(CoordinatorLayout, View, View, int, int, int[], int)}.
          This method will still continue to be called if the type is
          {@link ViewCompat#TYPE_TOUCH}."""
    )
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, target: View, dx: Int, dy: Int, consumed: IntArray
    ) {
        // Do nothing
    }

    /**
     * Called when a nested scroll in progress is about to update, before the target has
     * consumed any of the scrolled distance.
     *
     *
     * Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of [.onStartNestedScroll]. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     *
     *
     *
     * `onNestedPreScroll` is called each time the nested scroll is updated
     * by the nested scrolling child, before the nested scrolling child has consumed the scroll
     * distance itself. *Each Behavior responding to the nested scroll will receive the
     * same values.* The CoordinatorLayout will report as consumed the maximum number
     * of pixels in either direction that any Behavior responding to the nested scroll reported
     * as consumed.
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     * @param dx the raw horizontal number of pixels that the user attempted to scroll
     * @param dy the raw vertical number of pixels that the user attempted to scroll
     * @param consumed out parameter. consumed[0] should be set to the distance of dx that
     * was consumed, consumed[1] should be set to the distance of dy that
     * was consumed
     * @param type the type of input which cause this scroll event
     *
     * @see NestedScrollingParent2.onNestedPreScroll
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, target: View, dx: Int, dy: Int, consumed: IntArray,
        @ViewCompat.NestedScrollType type: Int
    ) {
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
        }
    }

    /**
     * Called when a nested scrolling child is starting a fling or an action that would
     * be a fling.
     *
     *
     * Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of [.onStartNestedScroll]. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     *
     *
     *
     * `onNestedFling` is called when the current nested scrolling child view
     * detects the proper conditions for a fling. It reports if the child itself consumed
     * the fling. If it did not, the child is expected to show some sort of overscroll
     * indication. This method should return true if it consumes the fling, so that a child
     * that did not itself take an action in response can choose not to show an overfling
     * indication.
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     * @param velocityX horizontal velocity of the attempted fling
     * @param velocityY vertical velocity of the attempted fling
     * @param consumed true if the nested child view consumed the fling
     * @return true if the Behavior consumed the fling
     *
     * @see NestedScrollingParent.onNestedFling
     */
    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, target: View, velocityX: Float, velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return false
    }

    /**
     * Called when a nested scrolling child is about to start a fling.
     *
     *
     * Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of [.onStartNestedScroll]. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     *
     *
     *
     * `onNestedPreFling` is called when the current nested scrolling child view
     * detects the proper conditions for a fling, but it has not acted on it yet. A
     * Behavior can return true to indicate that it consumed the fling. If at least one
     * Behavior returns true, the fling should not be acted upon by the child.
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param target the descendant view of the CoordinatorLayout performing the nested scroll
     * @param velocityX horizontal velocity of the attempted fling
     * @param velocityY vertical velocity of the attempted fling
     * @return true if the Behavior consumed the fling
     *
     * @see NestedScrollingParent.onNestedPreFling
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, target: View, velocityX: Float, velocityY: Float
    ): Boolean {
        return false
    }

    /**
     * Called when the window insets have changed.
     *
     *
     * Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to handle the window inset change on behalf of it's associated view.
     *
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child the child view of the CoordinatorLayout this Behavior is associated with
     * @param insets the new window insets.
     *
     * @return The insets supplied, minus any insets that were consumed
     */
    override fun onApplyWindowInsets(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, insets: WindowInsetsCompat
    ): WindowInsetsCompat {
        return insets
    }

    /**
     * Called when a child of the view associated with this behavior wants a particular
     * rectangle to be positioned onto the screen.
     *
     *
     * The contract for this method is the same as
     * [ViewParent.requestChildRectangleOnScreen].
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child             the child view of the CoordinatorLayout this Behavior is
     * associated with
     * @param rectangle         The rectangle which the child wishes to be on the screen
     * in the child's coordinates
     * @param immediate         true to forbid animated or delayed scrolling, false otherwise
     * @return true if the Behavior handled the request
     * @see ViewParent.requestChildRectangleOnScreen
     */
    override fun onRequestChildRectangleOnScreen(
        coordinatorLayout: CoordinatorLayout,
        child: AppCompatImageView, rectangle: Rect, immediate: Boolean
    ): Boolean {
        return false
    }

    /**
     * Hook allowing a behavior to re-apply a representation of its internal state that had
     * previously been generated by [.onSaveInstanceState]. This function will never
     * be called with a null state.
     *
     * @param parent the parent CoordinatorLayout
     * @param child child view to restore from
     * @param state The frozen state that had previously been returned by
     * [.onSaveInstanceState].
     *
     * @see .onSaveInstanceState
     */
    override fun onRestoreInstanceState(
        parent: CoordinatorLayout, child: AppCompatImageView,
        state: Parcelable
    ) {
        // no-op
    }

    /**
     * Hook allowing a behavior to generate a representation of its internal state
     * that can later be used to create a new instance with that same state.
     * This state should only contain information that is not persistent or can
     * not be reconstructed later.
     *
     *
     * Behavior state is only saved when both the parent [CoordinatorLayout] and
     * a view using this behavior have valid IDs set.
     *
     * @param parent the parent CoordinatorLayout
     * @param child child view to restore from
     *
     * @return Returns a Parcelable object containing the behavior's current dynamic
     * state.
     *
     * @see .onRestoreInstanceState
     * @see View.onSaveInstanceState
     */
    override fun onSaveInstanceState(
        parent: CoordinatorLayout,
        child: AppCompatImageView
    ): Parcelable? {
        return View.BaseSavedState.EMPTY_STATE
    }

    /**
     * Called when a view is set to dodge view insets.
     *
     *
     * This method allows a behavior to update the rectangle that should be dodged.
     * The rectangle should be in the parent's coordinate system and within the child's
     * bounds. If not, a [IllegalArgumentException] is thrown.
     *
     * @param parent the CoordinatorLayout parent of the view this Behavior is
     * associated with
     * @param child  the child view of the CoordinatorLayout this Behavior is associated with
     * @param rect   the rect to update with the dodge rectangle
     * @return true the rect was updated, false if we should use the child's bounds
     */
    override fun getInsetDodgeRect(
        parent: CoordinatorLayout, child: AppCompatImageView,
        rect: Rect
    ): Boolean {
        return false
    }
}