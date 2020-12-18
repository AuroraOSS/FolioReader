package com.folioreader.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.folioreader.Config
import com.folioreader.Constants
import com.folioreader.FolioReader
import com.folioreader.R
import com.folioreader.databinding.ActivityContentHighlightBinding
import com.folioreader.ui.fragment.HighlightFragment
import com.folioreader.ui.fragment.TableOfContentFragment
import com.folioreader.util.AppUtil.Companion.getSavedConfig
import com.folioreader.util.UiUtil
import org.readium.r2.shared.Publication

class ContentHighlightActivity : AppCompatActivity() {

    private lateinit var B: ActivityContentHighlightBinding

    private var mIsNightMode = false
    private var mConfig: Config? = null
    private var publication: Publication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        B = ActivityContentHighlightBinding.inflate(layoutInflater)
        setContentView(B.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        publication = intent.getSerializableExtra(Constants.PUBLICATION) as Publication?
        mConfig = getSavedConfig(this)
        mIsNightMode = mConfig != null && mConfig!!.isNightMode
        initViews()
    }

    private fun initViews() {
        UiUtil.setColorIntToDrawable(mConfig!!.themeColor, B.btnClose.drawable)
        B.layoutContentHighlights.setBackgroundDrawable(
            UiUtil.getShapeDrawable(
                mConfig!!.themeColor
            )
        )
        if (mIsNightMode) {
            B.toolbar.setBackgroundColor(Color.BLACK)

            B.btnContents.apply {
                background = UiUtil.createStateDrawable(
                    mConfig!!.themeColor,
                    ContextCompat.getColor(this@ContentHighlightActivity, R.color.black)
                )
                setTextColor(
                    UiUtil.getColorList(
                        ContextCompat.getColor(this@ContentHighlightActivity, R.color.black),
                        mConfig!!.themeColor
                    )
                )
            }

            B.btnHighlights.apply {
                background = UiUtil.createStateDrawable(
                    mConfig!!.themeColor,
                    ContextCompat.getColor(this@ContentHighlightActivity, R.color.black)
                )
                setTextColor(
                    UiUtil.getColorList(
                        ContextCompat.getColor(this@ContentHighlightActivity, R.color.black),
                        mConfig!!.themeColor
                    )
                )
            }

        } else {
            B.btnContents.apply {
                background = UiUtil.createStateDrawable(
                    mConfig!!.themeColor,
                    ContextCompat.getColor(this@ContentHighlightActivity, R.color.white)
                )
                setTextColor(
                    UiUtil.getColorList(
                        ContextCompat.getColor(this@ContentHighlightActivity, R.color.white),
                        mConfig!!.themeColor
                    )
                )
            }

            B.btnHighlights.apply {
                background = UiUtil.createStateDrawable(
                    mConfig!!.themeColor,
                    ContextCompat.getColor(this@ContentHighlightActivity, R.color.white)
                )
                setTextColor(
                    UiUtil.getColorList(
                        ContextCompat.getColor(this@ContentHighlightActivity, R.color.white),
                        mConfig!!.themeColor
                    )
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color: Int
            color = if (mIsNightMode) {
                ContextCompat.getColor(this, R.color.black)
            } else {
                val attrs = intArrayOf(android.R.attr.navigationBarColor)
                val typedArray = theme.obtainStyledAttributes(attrs)
                typedArray.getColor(0, ContextCompat.getColor(this, R.color.white))
            }
            window.navigationBarColor = color
        }

        loadContentFragment()

        B.btnClose.setOnClickListener { finish() }
        B.btnContents.setOnClickListener { loadContentFragment() }
        B.btnHighlights.setOnClickListener { loadHighlightsFragment() }
    }

    private fun loadContentFragment() {
        B.btnContents.isSelected = true
        B.btnHighlights.isSelected = false
        val contentFrameLayout = TableOfContentFragment.newInstance(
            publication,
            intent.getStringExtra(Constants.CHAPTER_SELECTED),
            intent.getStringExtra(Constants.BOOK_TITLE)
        )
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.parent, contentFrameLayout)
        ft.commit()
    }

    private fun loadHighlightsFragment() {
        B.btnContents.isSelected = false
        B.btnHighlights.isSelected = true
        val bookId = intent.getStringExtra(FolioReader.EXTRA_BOOK_ID)
        val bookTitle = intent.getStringExtra(Constants.BOOK_TITLE)
        val highlightFragment = HighlightFragment.newInstance(bookId, bookTitle)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.parent, highlightFragment)
        ft.commit()
    }
}