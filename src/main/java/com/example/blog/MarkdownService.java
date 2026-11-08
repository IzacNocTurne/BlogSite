package com.example.blog;

import java.util.List;
import java.util.regex.Pattern;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Code;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.CustomNode;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.Link;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

/**
 * 글 본문(마크다운)을 화면용 HTML과 요약용 일반 텍스트로 바꾼다.
 * 템플릿에서는 ${@markdown.toHtml(...)} 형태로 사용한다.
 */
@Component("markdown")
public class MarkdownService {

    // 예전 글에서 소제목으로 쓴 "■ 제목" 줄을 마크다운 소제목으로 취급
    private static final Pattern SQUARE_HEADING = Pattern.compile("(?m)^[ \\t]*■[ \\t]*(\\S.*)$");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    private final Parser parser;
    private final HtmlRenderer htmlRenderer;

    public MarkdownService() {
        List<Extension> extensions = List.of(TablesExtension.create(), AutolinkExtension.create());
        parser = Parser.builder().extensions(extensions).build();
        htmlRenderer = HtmlRenderer.builder()
                .extensions(extensions)
                .escapeHtml(true)          // 본문 속 HTML은 그대로 글자로 보여 줌
                .sanitizeUrls(true)        // javascript: 같은 링크 차단
                .softbreak("<br>\n")       // 줄바꿈만 한 기존 글도 줄이 유지되도록
                .attributeProviderFactory(context -> (node, tagName, attributes) -> {
                    if (node instanceof Link) {
                        attributes.put("target", "_blank");
                        attributes.put("rel", "nofollow noopener noreferrer");
                    }
                })
                .build();
    }

    public String toHtml(String markdown) {
        return htmlRenderer.render(parse(markdown));
    }

    /** 목록 카드, 메타 설명 등에 쓰는 서식 없는 요약 */
    public String excerpt(String markdown, int maxLength) {
        String text = WHITESPACE.matcher(plainText(parse(markdown))).replaceAll(" ").trim();
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, Math.max(maxLength - 3, 0)).trim() + "...";
    }

    // 글자만 모으고 블록 사이에는 공백을 넣음 (링크 주소, 서식 기호는 제외)
    private static String plainText(Node document) {
        StringBuilder out = new StringBuilder();
        document.accept(new AbstractVisitor() {
            @Override public void visit(Text text) { out.append(text.getLiteral()); }
            @Override public void visit(Code code) { out.append(code.getLiteral()); }
            @Override public void visit(SoftLineBreak softLineBreak) { out.append(" "); }
            @Override public void visit(HardLineBreak hardLineBreak) { out.append(" "); }
            @Override public void visit(Paragraph paragraph) { visitChildren(paragraph); out.append(" "); }
            @Override public void visit(Heading heading) { visitChildren(heading); out.append(" "); }
            @Override public void visit(ListItem listItem) { visitChildren(listItem); out.append(" "); }
            @Override public void visit(CustomBlock customBlock) { visitChildren(customBlock); out.append(" "); }
            @Override public void visit(CustomNode customNode) { visitChildren(customNode); out.append(" "); }
        });
        return out.toString();
    }

    private Node parse(String markdown) {
        String source = markdown == null ? "" : markdown;
        return parser.parse(SQUARE_HEADING.matcher(source).replaceAll("## $1"));
    }
}
