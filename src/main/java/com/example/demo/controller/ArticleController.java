package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import com.example.demo.form.ArticleForm;
import com.example.demo.form.CommentForm;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

/**
 * articleテーブルの情報を操作するコントローラ
 * 
 * @author masashi.nose
 *
 */
@Controller
@RequestMapping("/bbs")
public class ArticleController {

	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}

	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CommentRepository commentRepository;

	private List<Article> articleList;

	/**
	 * トップ画面を表示.
	 * 
	 * @param model リクエストスコープ作成
	 * @return トップ画面
	 */
	@RequestMapping("")
	public String index(Model model) {
//		記事を全件検索
		articleList = articleRepository.findAll();

//		検索して取り出したarticleListから1件1件articleを取り出す
		for (Article article : articleList) {

//			記事IDでコメントを検索し、リストへ格納
			List<Comment> commentList = commentRepository.findByArticleId(article.getId());

//			articleドメインのコメントリストに、検索結果のコメントリストを格納
			article.setCommentList(commentList);
			System.out.println(commentList);
		}

		model.addAttribute("articleList", articleList);

		return "bbs";
	}

	/**
	 * 記事を投稿する.
	 * 
	 * @param form リクエストパラメータ
	 * @return 投稿結果へリダイレクト
	 */
	@RequestMapping("/output")
	public String insertArticle(ArticleForm form) {
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		articleRepository.insert(article);
		return "redirect:/bbs";
	}

	/**
	 * 
	 * コメントを投稿する.
	 * 
	 * @param form リクエストパラメータ
	 * @return 投稿結果へリダイレクト
	 */
	@RequestMapping("/output-comment")
	public String insertComment(CommentForm form) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		comment.setArticleId(Integer.parseInt(form.getArticleId()));

		commentRepository.insert(comment);

		return "redirect:/bbs";
	}

	@RequestMapping("/delete-article")
	public String deleteArticle(Integer id) {
		commentRepository.deleteByArticleId(id);
		articleRepository.deleteById(id);
		return "redirect:/bbs";

	}

}
