(function(){
'use strict';

var mdEditor = {
  simpleRender: function(text) {
    var html = text
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/^### (.+)$/gm, '<h3>$1</h3>')
      .replace(/^## (.+)$/gm, '<h2>$1</h2>')
      .replace(/^# (.+)$/gm, '<h1>$1</h1>')
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.+?)\*/g, '<em>$1</em>')
      .replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>')
      .replace(/`([^`]+)`/g, '<code>$1</code>')
      .replace(/!\[([^\]]*)\]\(([^)]+)\)/g, '<img src="$2" alt="$1" style="max-width:100%">')
      .replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener noreferrer">$1</a>')
      .replace(/^> (.+)$/gm, '<blockquote>$1</blockquote>')
      .replace(/^- (.+)$/gm, '<li>$1</li>')
      .replace(/^\d+\. (.+)$/gm, '<li>$1</li>')
      .replace(/\n\n/g, '</p><p>')
      .replace(/\n/g, '<br>');
    return '<p>' + html + '</p>';
  },

  init: function(textarea) {
    if (!textarea || textarea.dataset.mdEditor === 'true') return;
    textarea.dataset.mdEditor = 'true';

    var wrapper = document.createElement('div');
    wrapper.className = 'md-editor-wrapper';
    wrapper.style.cssText = 'border:1px solid #ced4da;border-radius:6px;overflow:hidden;background:#fff;';

    var toolbar = document.createElement('div');
    toolbar.className = 'md-editor-toolbar';
    toolbar.style.cssText = 'display:flex;flex-wrap:wrap;gap:2px;padding:4px 6px;background:#f8f9fa;border-bottom:1px solid #dee2e6;';

    var buttons = [
      {cmd:'bold',      label:'<strong>B</strong>',   title:'Bold (Ctrl+B)',     wrap:['**','**']},
      {cmd:'italic',    label:'<em>I</em>',           title:'Italic (Ctrl+I)',   wrap:['*','*']},
      {cmd:'heading',   label:'H2',                   title:'Heading',           wrap:['## ','\n']},
      {cmd:'link',      label:'🔗',                   title:'Link',              wrap:['[text](',')']},
      {cmd:'code',      label:'<>',                   title:'Code',              wrap:['`','`']},
      {cmd:'blockquote',label:'❝',                   title:'Blockquote',        wrap:['> ','']},
      {cmd:'list',      label:'•',                    title:'List',              wrap:['- ','']},
      {cmd:'img',       label:'🖼',                   title:'Image',             wrap:['![alt](',')']}
    ];

    buttons.forEach(function(btn) {
      var el = document.createElement('button');
      el.type = 'button';
      el.innerHTML = btn.label;
      el.title = btn.title;
      el.style.cssText = 'border:none;background:transparent;border-radius:4px;padding:2px 8px;cursor:pointer;font-size:13px;font-weight:600;';
      el.onmouseover = function(){el.style.background='#e9ecef';};
      el.onmouseout = function(){el.style.background='transparent';};
      el.onclick = function(e){
        e.preventDefault();
        mdEditor.wrapText(textarea, btn.wrap[0], btn.wrap[1]);
        textarea.focus();
      };
      toolbar.appendChild(el);
    });

    var previewBtn = document.createElement('button');
    previewBtn.type = 'button';
    previewBtn.textContent = '👁 Preview';
    previewBtn.style.cssText = 'border:none;background:transparent;border-radius:4px;padding:2px 10px;cursor:pointer;font-size:13px;font-weight:600;margin-left:auto;color:#0d6efd;';
    previewBtn.onmouseover = function(){previewBtn.style.background='#e9ecef';};
    previewBtn.onmouseout = function(){previewBtn.style.background='transparent';};
    previewBtn.onclick = function(e){
      e.preventDefault();
      mdEditor.togglePreview(textarea, wrapper);
    };
    toolbar.appendChild(previewBtn);

    var parent = textarea.parentNode;
    parent.insertBefore(wrapper, textarea);
    wrapper.appendChild(toolbar);
    wrapper.appendChild(textarea);

    textarea.style.cssText = 'width:100%;min-height:180px;border:none;border-radius:0;padding:10px;font-family:monospace;font-size:14px;resize:vertical;outline:none;box-sizing:border-box;margin:0;';
  },

  wrapText: function(textarea, before, after) {
    var start = textarea.selectionStart;
    var end = textarea.selectionEnd;
    var text = textarea.value;
    var selected = text.substring(start, end);
    var replacement = before + selected + after;
    textarea.value = text.substring(0, start) + replacement + text.substring(end);
    textarea.selectionStart = start + before.length;
    textarea.selectionEnd = start + before.length + selected.length;
    textarea.dispatchEvent(new Event('input', {bubbles:true}));
  },

  togglePreview: function(textarea, wrapper) {
    var existing = wrapper.querySelector('.md-editor-preview');
    if (existing) {
      existing.remove();
      textarea.style.display = '';
      return;
    }
    var text = textarea.value || '';
    var preview = document.createElement('div');
    preview.className = 'md-editor-preview';
    preview.style.cssText = 'padding:10px;min-height:180px;font-size:14px;line-height:1.7;overflow-y:auto;background:#fff;';
    preview.innerHTML = mdEditor.simpleRender(text);
    textarea.style.display = 'none';
    wrapper.appendChild(preview);
  },

  autoInit: function() {
    var textareas = document.querySelectorAll('textarea[data-md-editor="true"]');
    [].forEach.call(textareas, function(ta) { mdEditor.init(ta); });
  }
};

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', mdEditor.autoInit);
} else {
  mdEditor.autoInit();
}
})();
