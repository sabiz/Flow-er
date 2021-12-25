package jp.sabiz.flow_er.extension

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.databinding.BindingAdapter
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

@BindingAdapter(value = ["layout_constraintGuide_percent"])
fun setConstraintPercent(view: Guideline, percent: Float) {
    val constraintLayout = view.parent as ConstraintLayout
    val constraintSet = ConstraintSet()
    constraintSet.clone(constraintLayout)
    constraintSet.setGuidelinePercent(view.id, percent)
    val autoTransition = AutoTransition()
    autoTransition.duration = 200

    TransitionManager.beginDelayedTransition(constraintLayout, autoTransition)
    constraintSet.applyTo(constraintLayout)
}