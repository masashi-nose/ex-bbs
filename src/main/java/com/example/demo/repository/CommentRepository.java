package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Comment;

/**
 * commentsテーブルの情報を操作するリポジトリ.
 * 
 * @author masashi.nose
 *
 */
@Repository
public class CommentRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final String TABLE_NAME = "comments";

	public static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
		Comment comment = new Comment();
		comment.setId(rs.getInt("id"));
		comment.setName(rs.getString("name"));
		comment.setContent(rs.getString("content"));
		comment.setArticleId(rs.getInt("article_id"));
		return comment;
	};

	/**
	 * 
	 * 記事IDで検索する.
	 * 
	 * @param articleId 記事ID
	 * @return コメント一覧
	 */
	public List<Comment> findByArticleId(Integer articleId) {
		String sql = "select id, name, content, article_id from " + TABLE_NAME + " where article_id =:articleId";

		SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);

		List<Comment> commentList = template.query(sql, param, COMMENT_ROW_MAPPER);

		return commentList;

	}

	/**
	 * コメントを投稿するメソッド.
	 * 
	 * @param comment リクエストパラメータ
	 */
	public void insert(Comment comment) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(comment);

		String insertSql = "insert into " + TABLE_NAME
				+ " (name, content, article_id) values (:name, :content, :articleId)";

		template.update(insertSql, param);
	}

	/**
	 * コメント削除するメソッド.
	 * 
	 * @param articleId 記事ID
	 */
	public void deleteByArticleId(Integer articleId) {
		String deleteSql = "delete from " + TABLE_NAME + " where article_id = :articleId";

		SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);

		template.update(deleteSql, param);

	}

}
