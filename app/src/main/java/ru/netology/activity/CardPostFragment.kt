package ru.netology.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.BuildConfig
import ru.netology.R
import ru.netology.databinding.FragmentCardPostBinding
import ru.netology.dto.AttachmentType
import ru.netology.dto.Post
import ru.netology.util.StringArg
import ru.netology.view.load
import ru.netology.view.loadCircleCrop
import ru.netology.viewmodel.PostViewModel

@AndroidEntryPoint
class CardPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCardPostBinding.inflate(inflater, container, false)

        val post: Post = arguments?.get("post") as Post
        post.let {
            binding.apply {
                author.text = post.author
                avatar.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")
                published.text = post.published
                content.text = post.content
                like.isChecked = post.likedByMe
                like.text = "${post.likes}"
                if (!post.video.isNullOrEmpty()) {
                    video.visibility = View.VISIBLE
                } else video.visibility = View.GONE

                if (post.attachment != null && post.attachment!!.type == AttachmentType.IMAGE) {
                    attachmentView.visibility = View.VISIBLE
                    attachmentView.load("${BuildConfig.BASE_URL}/media/${post.attachment!!.url}")
                } else attachmentView.visibility = View.GONE

                menu.setOnClickListener { it ->
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
                like.setOnClickListener {
                    viewModel.likeById(post.id)
                }
                repost.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plane"
                    }
                    val repostIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_repost))
                    startActivity(repostIntent)
                }
                video.setOnClickListener {
                    //viewModel.video()
                }
                //  like.text = displayNumbers(post.likes)
                //   repost.text = displayNumbers(post.reposts)

                attachmentView.setOnClickListener {
                    findNavController().navigate(R.id.action_cardPostFragment_to_fragmentImage,
                        Bundle().apply
                        {
                            textArg = post.attachment?.url
                        })
                }
            }
        }

        return binding.root
    }
}