package ru.btpit.nmedia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.btpit.nmedia.databinding.ActivityMain2Binding

import ru.btpit.nmedia.databinding.CardPostBinding


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        val bind: CardPostBinding = CardPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(
            object : onInteractionListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onShare(post: Post) {
                    val intent = Intent().apply{
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,post.content)
                        type = "text/plain"
                    }
                    val ShareIntent = Intent.createChooser(intent, "Поделиться")
                    startActivity(ShareIntent)
                }
            })
        binding.listCool.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.otpravitbtn.setOnClickListener{
            with(binding.editContent) {
                if (text.isNullOrBlank()) {
                    android.widget.Toast.makeText(
                        this@MainActivity2,
                        "Content can't be empty",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with (binding.editContent) {
                requestFocus()
                setText(post.content)
            }
        }
    }
}