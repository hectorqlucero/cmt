/* Display confirmation on clicking a delete button with the value 'Delete' */
$(document).ready(function () {
  $('.confirm').each(function () {
    var href = $(this).attr('href');
    $(this).click(function () {
      if (confirm("Esta usted seguro?")) {
        window.location.href = href;
      } else {
        return false;
      }
    });
  });

  /* Theme toggle (light/dark) */
  var body = $('body');
  var toggle = $('#theme-toggle');
  var stored = localStorage.getItem('theme');
  if (stored === 'dark') {
    body.removeClass('theme-light').addClass('theme-dark');
    toggle.html('<i class="bi bi-sun-fill"></i> Claro');
  } else {
    toggle.html('<i class="bi bi-moon-fill"></i> Oscuro');
  }

  toggle.on('click', function () {
    if (body.hasClass('theme-dark')) {
      body.removeClass('theme-dark').addClass('theme-light');
      localStorage.setItem('theme', 'light');
      toggle.html('<i class="bi bi-moon-fill"></i> Oscuro');
    } else {
      body.removeClass('theme-light').addClass('theme-dark');
      localStorage.setItem('theme', 'dark');
      toggle.html('<i class="bi bi-sun-fill"></i> Claro');
    }
  });

  /* Carousel professional enhancements */
  var $carousel = $('#carouselExample');
  if ($carousel.length && typeof bootstrap !== 'undefined' && bootstrap.Carousel) {
    var instance = bootstrap.Carousel.getOrCreateInstance($carousel[0]);
    var interval = parseInt($carousel.attr('data-bs-interval'), 10) || 5000;
    var $items = $carousel.find('.carousel-item');
    var totalSlides = $items.length;

    function startProgress() {
      var $bar = $carousel.find('.carousel-progress span');
      $bar.stop(true, true).css({ width: 0 });
      $bar.animate({ width: '100%' }, interval, 'linear');
    }

    function applyKenBurns() {
      $carousel.find('.carousel-item img').removeClass('kenburns-active');
      var $active = $carousel.find('.carousel-item.active img');
      $active.addClass('kenburns-active');
    }

    // Initial state
    startProgress();
    applyKenBurns();

    $carousel.on('slide.bs.carousel', function () {
      var $bar = $carousel.find('.carousel-progress span');
      $bar.stop(true, true).css({ width: 0 });
    });

    $carousel.on('slid.bs.carousel', function () {
      startProgress();
      applyKenBurns();
    });

    // Pause when cursor enters image area; resume when leaves
    $carousel.find('.carousel-inner').on('mouseenter', function () {
      instance.pause();
      var $bar = $carousel.find('.carousel-progress span');
      $bar.stop(true, true); // freeze progress
    }).on('mouseleave', function () {
      instance.cycle();
      // restart progress fresh
      startProgress();
    });

    // Accessible status updates
    var $status = $carousel.find('.carousel-status');
    function updateStatus() {
      if ($status.length) {
        var index = $items.index($items.filter('.active')) + 1;
        $status.text('Imagen ' + index + ' de ' + totalSlides);
      }
    }
    updateStatus();
    $carousel.on('slid.bs.carousel', updateStatus);

    // Dynamic caption with slide number
    var $counter = $carousel.find('.slide-counter');
    function updateCounter() {
      if ($counter.length) {
        var index = $items.index($items.filter('.active')) + 1;
        $counter.text(index + ' / ' + totalSlides);
      }
    }
    updateCounter();
    $carousel.on('slid.bs.carousel', updateCounter);

    // Subset functional indicators inside nav controls
    var $navContainer = $carousel.find('.carousel-nav');
    if ($navContainer.length && totalSlides > 0) {
      var maxDots = Math.min(8, totalSlides);

      // Create indicator dots container
      var $indicatorDots = $('<div class="indicator-dots">');

      // Add dots
      for (var i = 0; i < maxDots; i++) {
        var $dot = $('<button type="button">').attr('data-bs-target', '#carouselExample').attr('data-bs-slide-to', i);
        if (i === 0) $dot.addClass('active');
        $indicatorDots.append($dot);
      }

      // Add +N indicator if needed
      if (totalSlides > maxDots) {
        $indicatorDots.append($('<span class="indicator-more">+' + (totalSlides - maxDots) + '</span>'));
      }

      // Insert indicator dots between prev and next buttons
      $navContainer.find('.carousel-control-prev').after($indicatorDots);

      function updateIndicators() {
        var index = $items.index($items.filter('.active'));
        $indicatorDots.find('button').removeClass('active');
        if (index < maxDots) {
          $indicatorDots.find('button').eq(index).addClass('active');
        }
      }
      $carousel.on('slid.bs.carousel', updateIndicators);
    }

    // Lazy prefetch optimization (prefetch next image)
    function prefetchNext() {
      var $active = $carousel.find('.carousel-item.active');
      var $next = $active.next('.carousel-item');
      if (!$next.length) $next = $carousel.find('.carousel-item').first();
      var $img = $next.find('img');
      if ($img.length && $img.attr('loading') === 'lazy') {
        var src = $img.attr('src');
        if (src && !$img.data('prefetched')) {
          var link = document.createElement('link');
          link.rel = 'prefetch';
          link.href = src;
          link.as = 'image';
          document.head.appendChild(link);
          $img.data('prefetched', true);
        }
      }
    }
    prefetchNext();
    $carousel.on('slid.bs.carousel', prefetchNext);

    // Enhanced touch swipe with mobile optimizations
    var touchStartX = null;
    var touchStartY = null;
    var touchStartTime = null;
    var threshold = 50; // px horizontal
    var maxVerticalThreshold = 30; // px vertical tolerance
    var minSwipeTime = 100; // ms minimum swipe time
    var maxSwipeTime = 500; // ms maximum swipe time

    $carousel[0].addEventListener('touchstart', function (e) {
      if (e.touches && e.touches.length === 1) {
        touchStartX = e.touches[0].clientX;
        touchStartY = e.touches[0].clientY;
        touchStartTime = Date.now();
        // Prevent default to avoid scrolling conflicts
        e.preventDefault();
      }
    }, { passive: false });

    $carousel[0].addEventListener('touchmove', function (e) {
      // Allow vertical scrolling if not a clear horizontal swipe
      if (!touchStartX || !touchStartY || !touchStartTime) return;

      var dx = e.touches[0].clientX - touchStartX;
      var dy = e.touches[0].clientY - touchStartY;
      var elapsedTime = Date.now() - touchStartTime;

      // Check if it's a valid horizontal swipe
      if (Math.abs(dx) > threshold &&
        Math.abs(dx) > Math.abs(dy) &&
        Math.abs(dy) < maxVerticalThreshold &&
        elapsedTime > minSwipeTime &&
        elapsedTime < maxSwipeTime) {

        // Determine swipe direction
        if (dx > 0) {
          instance.prev();
        } else {
          instance.next();
        }

        // Reset touch state
        touchStartX = null;
        touchStartY = null;
        touchStartTime = null;

        // Prevent default to stop scrolling
        e.preventDefault();
      }
    }, { passive: false });

    $carousel[0].addEventListener('touchend', function () {
      touchStartX = null;
      touchStartY = null;
      touchStartTime = null;
    });

    // Add passive touch handling for better performance on mobile
    $carousel[0].addEventListener('touchcancel', function () {
      touchStartX = null;
      touchStartY = null;
      touchStartTime = null;
    });
  }
});


