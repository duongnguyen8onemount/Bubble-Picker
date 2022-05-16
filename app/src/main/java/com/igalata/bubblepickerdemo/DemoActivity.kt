package com.igalata.bubblepickerdemo

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.igalata.bubblepicker.BubblePickerListener
import com.igalata.bubblepicker.adapter.BubblePickerAdapter
import com.igalata.bubblepicker.model.BubbleGradient
import com.igalata.bubblepicker.model.PickerItem
import kotlinx.android.synthetic.main.activity_demo.addButton
import kotlinx.android.synthetic.main.activity_demo.hintTextView
import kotlinx.android.synthetic.main.activity_demo.picker
import kotlinx.android.synthetic.main.activity_demo.subtitleTextView
import kotlinx.android.synthetic.main.activity_demo.titleTextView
import kotlin.random.Random


/**
 * Created by irinagalata on 1/19/17.
 */
class DemoActivity : AppCompatActivity() {

    private val boldTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_BOLD) }
    private val mediumTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_MEDIUM) }
    private val regularTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_REGULAR) }

    companion object {
        private const val ROBOTO_BOLD = "roboto_bold.ttf"
        private const val ROBOTO_MEDIUM = "roboto_medium.ttf"
        private const val ROBOTO_REGULAR = "roboto_regular.ttf"
    }


    val removedTitles = mutableListOf<String>()
    var items = mutableListOf<String>()
    lateinit var images: TypedArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        images = resources.obtainTypedArray(R.array.images)
        titleTextView.typeface = mediumTypeface
        subtitleTextView.typeface = boldTypeface
        hintTextView.typeface = regularTypeface
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subtitleTextView.letterSpacing = 0.06f
            hintTextView.letterSpacing = 0.05f
        }

        items = resources.getStringArray(R.array.countries).toMutableList()

        picker.adapter = object : BubblePickerAdapter {

            override val totalCount = items.size

            override fun getItem(position: Int): PickerItem {
                return PickerItem().apply {
                    title = items[position]
                    typeface = mediumTypeface
                    gradient = BubbleGradient(
                        getRandomColor(),
                        getRandomColor(),
                        BubbleGradient.VERTICAL
                    )
                    textColor = ContextCompat.getColor(this@DemoActivity, android.R.color.white)
                    backgroundImage = ContextCompat.getDrawable(
                        this@DemoActivity,
                        images.getResourceId(position, 0)
                    )
                    borderWidth = 8f
                    borderColor = Color.parseColor("#41EB91")
                }
            }
        }

        picker.bubbleSize = 20
        picker.listener = object : BubblePickerListener {
            override fun onBubbleSelected(item: PickerItem) = toast("${item.title} selected")

            override fun onBubbleDeselected(item: PickerItem) = toast("${item.title} deselected")

            override fun onBubbleRemoved(item: PickerItem) {
                var mTitle = item.title ?: return
                toast("$mTitle removed")
                removedTitles.add(mTitle)
            }
        }

        addButton.setOnClickListener {
            items.add(items[Random.nextInt(items.size)])
            picker.addedItem(items.size - 1)
        }
    }

    override fun onResume() {
        super.onResume()
        picker.onResume()
    }

    override fun onPause() {
        super.onPause()
        picker.onPause()
    }

    fun getRandomColor(): Int {
        val colors = resources.getIntArray(R.array.colors)
        return colors[Random.nextInt(colors.size)]
    }

    private fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

}