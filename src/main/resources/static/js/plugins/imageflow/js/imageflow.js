/* ImageFlow constructor */
function ImageFlow() {
    /* Setting option defaults */
    this.defaults =
	{
	    animationSpeed: 50,         /* Animation speed in ms */
	    aspectRatio: 0.9,           /* Aspect ratio of the ImageFlow container (width divided by height) */
	    circular: true,             /* 循环 */
	    imageCursor: 'pointer',     /* Cursor type for all images - default is 'default' */
	    ImageFlowID: 'imageflow',   /* Default id of the ImageFlow container */
	    imageFocusM: 1.0,           /* Multiplicator for the focussed image size in percent */
	    imageFocusMax: 2,           /* 左右二侧图片数量 */
	    imagesHeight: 0.92,         /* 图片高度占DIV高度的比例 */
	    imagesM: 1.2,               /* 图片深度 */
	    onClick: function () { document.location = this.url; },   /* Onclick behaviour */
	    opacity: false,             /* Toggle image opacity */
	    opacityArray: [10, 8, 6, 4, 2],   /* Image opacity (range: 0 to 10) first value is for the focussed image */
	    percentOther: 224,          /* Scale portrait and square format */
	    preloadImages: true,        /* Toggles loading bar (false: requires img attributes height and width) */
	    reflections: true,          /* Toggle reflections */
	    slider: true,               /* Toggle slider */
	    slideshow: true,            /* Toggle slideshow */
	    slideshowSpeed: 3000,       /* Time between slides in ms */
	    slideshowAutoplay: true,    /* Toggle automatic slideshow play on startup */
	    startID: 1,                 /* Image ID to begin with */
	    glideToStartID: true,       /* Toggle glide animation to start ID */
	    startAnimation: false,      /* Animate images moving in from the right on startup */
	    xStep: 90                   /* Step width on the x-axis in px */
	};


    /* Closure for this */
    var my = this;


    /* Initiate ImageFlow */
    this.init = function (options) {
        /* Evaluate options */
        for (var name in my.defaults) {
            this[name] = (options !== undefined && options[name] !== undefined) ? options[name] : my.defaults[name];
        }

        /* Try to get ImageFlow div element */
        var ImageFlowDiv = document.getElementById(my.ImageFlowID);
        if (ImageFlowDiv) {
            /* Set it global within the ImageFlow scope */
            ImageFlowDiv.style.visibility = 'visible';
            this.ImageFlowDiv = ImageFlowDiv;

            /* Try to create XHTML structure */
            if (this.createStructure()) {
                this.imagesDiv = document.getElementById(my.ImageFlowID + '_images');

                this.indexArray = [];
                this.current = 0;
                this.imageID = 0;
                this.target = 0;
                this.memTarget = 0;
                this.firstRefresh = true;
                this.firstCheck = true;
                this.busy = false;

                /* Set height of the ImageFlow container and center the loading bar */
                var width = this.ImageFlowDiv.offsetWidth;
                var height = Math.round(width / my.aspectRatio);
                document.getElementById(my.ImageFlowID + '_loading_txt').style.paddingTop = ((height * 0.5) - 22) + 'px';
                ImageFlowDiv.style.height = height + 'px';

                /* Init loading progress */
                this.loadingProgress();
            }
        }
    };


    /* Create HTML Structure */
    this.createStructure = function () {
        /* Create images div container */
        var imagesDiv = my.Helper.createDocumentElement('div', 'images');

        /* Shift all images into the images div */
        var node, version, src, imageNode;
        var max = my.ImageFlowDiv.childNodes.length;
        for (var index = 0; index < max; index++) {
            node = my.ImageFlowDiv.childNodes[index];
            if (node && node.nodeType == 1 && node.nodeName == 'IMG') {
                /* Clone image nodes and append them to the images div */
                imageNode = node.cloneNode(true);
                imagesDiv.appendChild(imageNode);
            }
        }

        /* Clone some more images to make a circular animation possible */
        if (my.circular) {
            /* Create temporary elements to hold the cloned images */
            var first = my.Helper.createDocumentElement('div', 'images');
            var last = my.Helper.createDocumentElement('div', 'images');

            /* Make sure, that there are enough images to use circular mode */
            max = imagesDiv.childNodes.length;
            if (max < my.imageFocusMax) {
                my.imageFocusMax = max;
            }

            /* Do not clone anything if there is only one image */
            if (max > 1) {
                /* Clone the first and last images */
                var i;
                for (i = 0; i < max; i++) {
                    /* Number of clones on each side equals the imageFocusMax */
                    node = imagesDiv.childNodes[i];
                    if (i < my.imageFocusMax) {
                        imageNode = node.cloneNode(true);
                        first.appendChild(imageNode);
                    }
                    if (max - i < my.imageFocusMax + 1) {
                        imageNode = node.cloneNode(true);
                        last.appendChild(imageNode);
                    }
                }

                /* Sort the image nodes in the following order: last | originals | first */
                for (i = 0; i < max; i++) {
                    node = imagesDiv.childNodes[i];
                    imageNode = node.cloneNode(true);
                    last.appendChild(imageNode);
                }
                for (i = 0; i < my.imageFocusMax; i++) {
                    node = first.childNodes[i];
                    imageNode = node.cloneNode(true);
                    last.appendChild(imageNode);
                }

                /* Overwrite the imagesDiv with the new order */
                imagesDiv = last;
            }
        }

        /* Create slideshow button div and append it to the images div */
        if (my.slideshow) {
            var slideshowButton = my.Helper.createDocumentElement('div', 'slideshow');
            imagesDiv.appendChild(slideshowButton);
        }

        /* Create loading text container */
        var loadingP = my.Helper.createDocumentElement('p', 'loading_txt');
        var loadingText = document.createTextNode(' ');
        loadingP.appendChild(loadingText);

        /* Create loading div container */
        var loadingDiv = my.Helper.createDocumentElement('div', 'loading');

        /* Create loading bar div container inside the loading div */
        var loadingBarDiv = my.Helper.createDocumentElement('div', 'loading_bar');
        loadingDiv.appendChild(loadingBarDiv);


        /* Update document structure and return true on success */
        var success = false;
        if (my.ImageFlowDiv.appendChild(imagesDiv) &&
			my.ImageFlowDiv.appendChild(loadingP) &&
			my.ImageFlowDiv.appendChild(loadingDiv)) {
            /* Remove image nodes outside the images div */
            max = my.ImageFlowDiv.childNodes.length;
            for (index = 0; index < max; index++) {
                node = my.ImageFlowDiv.childNodes[index];
                if (node && node.nodeType == 1 && node.nodeName == 'IMG') {
                    my.ImageFlowDiv.removeChild(node);
                }
            }
            success = true;
        }
        return success;
    };


    /* Manage loading progress and call the refresh function */
    this.loadingProgress = function () {
        var p = my.loadingStatus();
        if ((p < 100 || my.firstCheck) && my.preloadImages) {
            /* Insert a short delay if the browser loads rapidly from its cache */
            if (my.firstCheck && p == 100) {
                my.firstCheck = false;
                window.setTimeout(my.loadingProgress, 100);
            }
            else {
                window.setTimeout(my.loadingProgress, 40);
            }
        }
        else {
            /* Hide loading elements */
            document.getElementById(my.ImageFlowID + '_loading_txt').style.display = 'none';
            document.getElementById(my.ImageFlowID + '_loading').style.display = 'none';

            /* Refresh ImageFlow on window resize - delay adding this event for the IE */
            window.setTimeout(my.Helper.addResizeEvent, 1000);

            /* Call refresh once on startup to display images */
            my.refresh();

            /* Only initialize navigation elements if there is more than one image */
            if (my.max > 1) {
                /* Initialize mouse, touch and key support */
                my.MouseWheel.init();
                my.Key.init();

                /* Toggle slideshow */
                if (my.slideshow) {
                    my.Slideshow.init();
                }
            }
        }
    };


    /* Return loaded images in percent, set loading bar width and loading text */
    this.loadingStatus = function () {
        var max = my.imagesDiv.childNodes.length;
        var i = 0, completed = 0;
        var image = null;
        for (var index = 0; index < max; index++) {
            image = my.imagesDiv.childNodes[index];
            if (image && image.nodeType == 1 && image.nodeName == 'IMG') {
                if (image.complete) {
                    completed++;
                }
                i++;
            }
        }

        var finished = Math.round((completed / i) * 100);
        var loadingBar = document.getElementById(my.ImageFlowID + '_loading_bar');
        loadingBar.style.width = finished + '%';

        /* Do not count the cloned images */
        if (my.circular) {
            i = i - (my.imageFocusMax * 2);
            completed = (finished < 1) ? 0 : Math.round((i / 100) * finished);
        }

        var loadingP = document.getElementById(my.ImageFlowID + '_loading_txt');
        var loadingTxt = document.createTextNode('正在加载,请稍候 ' + completed + '/' + i);
        loadingP.replaceChild(loadingTxt, loadingP.firstChild);
        return finished;
    };


    /* Cache EVERYTHING that only changes on refresh or resize of the window */
    this.refresh = function () {
        /* Cache global variables */
        this.imagesDivWidth = my.imagesDiv.offsetWidth + my.imagesDiv.offsetLeft;
        this.maxHeight = Math.round(my.imagesDivWidth / my.aspectRatio);
        this.maxFocus = my.imageFocusMax * my.xStep;
        this.size = my.imagesDivWidth * 0.5;
        this.imagesDivHeight = Math.round(my.maxHeight * my.imagesHeight);

        /* Change imageflow div properties */
        my.ImageFlowDiv.style.height = my.maxHeight + 'px';

        /* Change images div properties */
        my.imagesDiv.style.height = my.imagesDivHeight + 'px';

        /* Set image attributes */
        var max = my.imagesDiv.childNodes.length;
        var i = 0;
        var image = null;
        for (var index = 0; index < max; index++) {
            image = my.imagesDiv.childNodes[index];
            if (image !== null && image.nodeType == 1 && image.nodeName == 'IMG') {
                this.indexArray[i] = index;

                /* Set image attributes to store values */
                image.url = image.getAttribute('longdesc');
                image.xPosition = (-i * my.xStep);
                image.i = i;

                /* Add width and height as attributes only once */
                if (my.firstRefresh) {
                    if (image.getAttribute('width') !== null && image.getAttribute('height') !== null) {
                        image.w = image.getAttribute('width');
                        image.h = image.getAttribute('height');
                    }
                    else {
                        image.w = image.width;
                        image.h = image.height;
                    }
                }

                /* Portrait and square format */
                image.pc = my.percentOther;
                image.pcMem = my.percentOther;

                /* Set image cursor type */
                image.style.cursor = my.imageCursor;
                i++;
            }
        }
        this.max = my.indexArray.length;

        /* Handle startID on the first refresh */
        if (my.firstRefresh) {
            /* Reset variable */
            my.firstRefresh = false;

            /* Set imageID to the startID */
            my.imageID = my.startID - 1;
            if (my.imageID < 0) {
                my.imageID = 0;
            }

            /* Map image id range in cicular mode (ignore the cloned images) */
            if (my.circular) {
                my.imageID = my.imageID + my.imageFocusMax;
            }

            /* Make sure, that the id is smaller than the image count  */
            maxId = (my.circular) ? (my.max - (my.imageFocusMax)) - 1 : my.max - 1;
            if (my.imageID > maxId) {
                my.imageID = maxId;
            }

            /* Toggle glide animation to start ID */
            if (my.glideToStartID === false) {
                my.moveTo(-my.imageID * my.xStep);
            }

            /* Animate images moving in from the right */
            if (my.startAnimation) {
                my.moveTo(5000);
            }
        }

        /* Only animate if there is more than one image */
        if (my.max > 1) {
            my.glideTo(my.imageID);
        }

        /* Display images in current order */
        my.moveTo(my.current);
    };


    /* Main animation function */
    this.moveTo = function (x) {
        this.current = x;
        this.zIndex = my.max;

        /* Main loop */
        for (var index = 0; index < my.max; index++) {
            var image = my.imagesDiv.childNodes[my.indexArray[index]];
            var currentImage = index * -my.xStep;

            /* Don't display images that are not conf_focussed */
            if ((currentImage + my.maxFocus) < my.memTarget || (currentImage - my.maxFocus) > my.memTarget) {
                try {
                    image.style.visibility = 'hidden';
                    image.style.display = 'none';
                }
                catch (e)
                { }
            }
            else {
                try {
                    var z = (Math.sqrt(10000 + x * x) + 100) * my.imagesM;
                    var xs = x / z * my.size + my.size;

                    /* Still hide images until they are processed, but set display style to block */
                    image.style.display = 'block';

                    /* Process new image height and width */
                    var newImageH = (image.h / image.w * image.pc) / z * my.size;
                    var newImageW = 0;
                    switch (newImageH > my.maxHeight) {
                        case false:
                            newImageW = image.pc / z * my.size;
                            break;

                        default:
                            newImageH = my.maxHeight;
                            newImageW = image.w * newImageH / image.h;
                            break;
                    }

                    var newImageTop = (my.imagesDivHeight - newImageH) / 2;

                    if (newImageTop <= 0) {
                        console.log(newImageTop)
                    }

                    /* Set new image properties */
                    image.style.left = xs - (image.pc / 2) / z * my.size + 'px';
                    if (newImageW && newImageH) {
                        image.style.height = newImageH + 'px';
                        image.style.width = newImageW + 'px';
                        image.style.top = newImageTop + 'px';
                    }
                    image.style.visibility = 'visible';

                    /* Set image layer through zIndex */
                    switch (x < 0) {
                        case true:
                            this.zIndex++;
                            break;

                        default:
                            this.zIndex = my.zIndex - 1;
                            break;
                    }

                    /* Change zIndex and onclick function of the focussed image */
                    switch (image.i == my.imageID) {
                        case false:
                            image.onclick = function () { my.glideTo(this.i); };
                            break;

                        default:
                            this.zIndex = my.zIndex + 1;
                            if (image.url !== '') {
                                image.onclick = my.onClick;
                            }
                            break;
                    }
                    image.style.zIndex = my.zIndex;
                }
                catch (e)
                { }
            }

            x += my.xStep;
        }
    };


    /* Initializes image gliding animation */
    this.glideTo = function (imageID)
    {
        /* Check for jumppoints */
        var jumpTarget, clonedImageID;
        if (my.circular)
        {
            /* Trigger left jumppoint */
            if (imageID + 1 === my.imageFocusMax)
            {
                /* Set jump target to the same cloned image on the right */
                clonedImageID = my.max - my.imageFocusMax;
                jumpTarget = -clonedImageID * my.xStep;

                /* Set the imageID to the last image */
                imageID = clonedImageID - 1;
            }

            /* Trigger right jumppoint */
            if (imageID === (my.max - my.imageFocusMax))
            {
                /* Set jump target to the same cloned image on the left */
                clonedImageID = my.imageFocusMax - 1;
                jumpTarget = -clonedImageID * my.xStep;

                /* Set the imageID to the first image */
                imageID = clonedImageID + 1;
            }
        }

        /* Calculate new image position target */
        var x = -imageID * my.xStep;
        this.target = x;
        this.memTarget = x;
        this.imageID = imageID;

        /* Only process if opacity or a multiplicator for the focussed image has been set */
        if (my.opacity === true || my.imageFocusM !== my.defaults.imageFocusM)
        {
            /* Set opacity for centered image */
            my.Helper.setOpacity(my.imagesDiv.childNodes[imageID], my.opacityArray[0]);
            my.imagesDiv.childNodes[imageID].pc = my.imagesDiv.childNodes[imageID].pc * my.imageFocusM;

            /* Set opacity for the other images that are displayed */
            var opacityValue = 0;
            var rightID = 0;
            var leftID = 0;
            var last = my.opacityArray.length;

            for (var i = 1; i < (my.imageFocusMax + 1); i++)
            {
                if ((i + 1) > last)
                {
                    opacityValue = my.opacityArray[last - 1];
                }
                else
                {
                    opacityValue = my.opacityArray[i];
                }

                rightID = imageID + i;
                leftID = imageID - i;

                if (rightID < my.max)
                {
                    my.Helper.setOpacity(my.imagesDiv.childNodes[rightID], opacityValue);
                    my.imagesDiv.childNodes[rightID].pc = my.imagesDiv.childNodes[rightID].pcMem;
                }
                if (leftID >= 0)
                {
                    my.Helper.setOpacity(my.imagesDiv.childNodes[leftID], opacityValue);
                    my.imagesDiv.childNodes[leftID].pc = my.imagesDiv.childNodes[leftID].pcMem;
                }
            }
        }

        /* Move the images to the jump target */
        if (jumpTarget)
        {
            my.moveTo(jumpTarget);
        }

        /* Animate gliding to new x position */
        if (my.busy === false)
        {
            my.busy = true;
            my.animate();
        }
    };


    /* Animates image gliding */
    this.animate = function () {
        switch (my.target < my.current - 1 || my.target > my.current + 1) {
            case true:
                my.moveTo(my.current + (my.target - my.current) / 3);
                window.setTimeout(my.animate, my.animationSpeed);
                my.busy = true;
                break;

            default:
                my.busy = false;
                break;
        }
    };


    /* Used by user events to call the glideTo function */
    this.glideOnEvent = function (imageID) {
        /* Interrupt slideshow on mouse wheel, keypress, touch and mouse drag */
        if (my.slideshow) {
            my.Slideshow.interrupt();
        }

        /* Glide to new imageID */
        my.glideTo(imageID);
    };


    /* Slideshow function */
    this.Slideshow =
	{
	    direction: 1,

	    init: function () {
	        /* Call start() if autoplay is enabled, stop() if it is disabled */
	        (my.slideshowAutoplay) ? my.Slideshow.start() : my.Slideshow.stop();
	    },

	    interrupt: function () {
	        /* Remove interrupt event */
	        my.Helper.removeEvent(my.ImageFlowDiv, 'click', my.Slideshow.interrupt);

	        /* Interrupt the slideshow */
	        my.Slideshow.stop();
	    },

	    addInterruptEvent: function () {
	        /* A click anywhere inside the ImageFlow div interrupts the slideshow */
	        my.Helper.addEvent(my.ImageFlowDiv, 'click', my.Slideshow.interrupt);
	    },

	    start: function () {
	        /* Set slide interval */
	        my.Slideshow.action = window.setInterval(my.Slideshow.slide, my.slideshowSpeed);

	        /* Allow the user to always interrupt the slideshow */
	        window.setTimeout(my.Slideshow.addInterruptEvent, 100);
	    },

	    stop: function () {
	        /* Clear slide interval */
	        window.clearInterval(my.Slideshow.action);
	    },

	    slide: function () {
	        var newImageID = my.imageID + my.Slideshow.direction;
	        var reverseDirection = false;

	        /* Reverse direction at the last image on the right */
	        if (newImageID === my.max) {
	            my.Slideshow.direction = -1;
	            reverseDirection = true;
	        }

	        /* Reverse direction at the last image on the left */
	        if (newImageID < 0) {
	            my.Slideshow.direction = 1;
	            reverseDirection = true;
	        }

	        /* If direction is reversed recall this method, else call the glideTo method */
	        (reverseDirection) ? my.Slideshow.slide() : my.glideTo(newImageID);
	    }
	};


    /* Mouse Wheel support */
    this.MouseWheel =
	{
	    init: function () {
	        /* Init mouse wheel listener */
	        if (window.addEventListener) {
	            my.ImageFlowDiv.addEventListener('DOMMouseScroll', my.MouseWheel.get, false);
	        }
	        my.Helper.addEvent(my.ImageFlowDiv, 'mousewheel', my.MouseWheel.get);
	    },

	    get: function (event) {
	        var delta = 0;
	        if (!event) {
	            event = window.event;
	        }
	        if (event.wheelDelta) {
	            delta = event.wheelDelta / 120;
	        }
	        else if (event.detail) {
	            delta = -event.detail / 3;
	        }
	        if (delta) {
	            my.MouseWheel.handle(delta);
	        }
	        my.Helper.suppressBrowserDefault(event);
	    },

	    handle: function (delta) {
	        var change = false;
	        var newImageID = 0;
	        if (delta > 0) {
	            if (my.imageID >= 1) {
	                newImageID = my.imageID - 1;
	                change = true;
	            }
	        }
	        else {
	            if (my.imageID < (my.max - 1)) {
	                newImageID = my.imageID + 1;
	                change = true;
	            }
	        }

	        /* Glide to next (mouse wheel down) / previous (mouse wheel up) image  */
	        if (change) {
	            my.glideOnEvent(newImageID);
	        }
	    }
	};

    /* Key support */
    this.Key =
	{
	    /* Init key event listener */
	    init: function () {
	        document.onkeydown = function (event) { my.Key.handle(event); };
	    },

	    /* Handle the arrow keys */
	    handle: function (event) {
	        var charCode = my.Key.get(event);
	        switch (charCode) {
	            /* Right arrow key */ 
	            case 39:
	                my.MouseWheel.handle(-1);
	                break;

	            /* Left arrow key */ 
	            case 37:
	                my.MouseWheel.handle(1);
	                break;
	        }
	    },

	    /* Get the current keycode */
	    get: function (event) {
	        event = event || window.event;
	        return event.keyCode;
	    }
	};


    /* Helper functions */
    this.Helper =
	{
	    /* Add events */
	    addEvent: function (obj, type, fn) {
	        if (obj.addEventListener) {
	            obj.addEventListener(type, fn, false);
	        }
	        else if (obj.attachEvent) {
	            obj["e" + type + fn] = fn;
	            obj[type + fn] = function () { obj["e" + type + fn](window.event); };
	            obj.attachEvent("on" + type, obj[type + fn]);
	        }
	    },

	    /* Remove events */
	    removeEvent: function (obj, type, fn) {
	        if (obj.removeEventListener) {
	            obj.removeEventListener(type, fn, false);
	        }
	        else if (obj.detachEvent) {
	            /* The IE breaks if you're trying to detach an unattached event /msdn.microsoft.com/en-us/library/ms536411(VS.85).aspx */
	            if (obj[type + fn] === undefined) {
	                alert('Helper.removeEvent » Pointer to detach event is undefined - perhaps you are trying to detach an unattached event?');
	            }
	            obj.detachEvent('on' + type, obj[type + fn]);
	            obj[type + fn] = null;
	            obj['e' + type + fn] = null;
	        }
	    },

	    /* Set image opacity */
	    setOpacity: function (object, value) {
	        if (my.opacity === true) {
	            object.style.opacity = value / 10;
	            object.style.filter = 'alpha(opacity=' + value * 10 + ')';
	        }
	    },

	    /* Create HTML elements */
	    createDocumentElement: function (type, id, optionalClass) {
	        var element = document.createElement(type);
	        element.setAttribute('id', my.ImageFlowID + '_' + id);
	        if (optionalClass !== undefined) {
	            id += ' ' + optionalClass;
	        }
	        my.Helper.setClassName(element, id);
	        return element;
	    },

	    /* Set CSS class */
	    setClassName: function (element, className) {
	        if (element) {
	            element.setAttribute('class', className);
	            element.setAttribute('className', className);
	        }
	    },

	    /* Suppress default browser behaviour to avoid image/text selection while dragging */
	    suppressBrowserDefault: function (e) {
	        if (e.preventDefault) {
	            e.preventDefault();
	        }
	        else {
	            e.returnValue = false;
	        }
	        return false;
	    },

	    /* Add functions to the window.onresize event - can not be done by addEvent */
	    addResizeEvent: function () {
	        var otherFunctions = window.onresize;
	        if (typeof window.onresize != 'function') {
	            window.onresize = function () {
	                my.refresh();
	            };
	        }
	        else {
	            window.onresize = function () {
	                if (otherFunctions) {
	                    otherFunctions();
	                }
	                my.refresh();
	            };
	        }
	    }
	};
}

/* DOMContentLoaded event handler - by Tanny O'Haley [4] */
var domReadyEvent =
{
    name: "domReadyEvent",
    /* Array of DOMContentLoaded event handlers.*/
    events: {},
    domReadyID: 1,
    bDone: false,
    DOMContentLoadedCustom: null,

    /* Function that adds DOMContentLoaded listeners to the array.*/
    add: function (handler) {
        /* Assign each event handler a unique ID. If the handler has an ID, it has already been added to the events object or been run.*/
        if (!handler.$$domReadyID) {
            handler.$$domReadyID = this.domReadyID++;

            /* If the DOMContentLoaded event has happened, run the function. */
            if (this.bDone) {
                handler();
            }

            /* store the event handler in the hash table */
            this.events[handler.$$domReadyID] = handler;
        }
    },

    remove: function (handler) {
        /* Delete the event handler from the hash table */
        if (handler.$$domReadyID) {
            delete this.events[handler.$$domReadyID];
        }
    },

    /* Function to process the DOMContentLoaded events array. */
    run: function () {
        /* quit if this function has already been called */
        if (this.bDone) {
            return;
        }

        /* Flag this function so we don't do the same thing twice */
        this.bDone = true;

        /* iterates through array of registered functions */
        for (var i in this.events) {
            this.events[i]();
        }
    },

    schedule: function () {
        /* Quit if the init function has already been called*/
        if (this.bDone) {
            return;
        }

        /* First, check for Safari or KHTML.*/
        if (/KHTML|WebKit/i.test(navigator.userAgent)) {
            if (/loaded|complete/.test(document.readyState)) {
                this.run();
            }
            else {
                /* Not ready yet, wait a little more.*/
                setTimeout(this.name + ".schedule()", 100);
            }
        }
        else if (document.getElementById("__ie_onload")) {
            /* Second, check for IE.*/
            return true;
        }

        /* Check for custom developer provided function.*/
        if (typeof this.DOMContentLoadedCustom === "function") {
            /* if DOM methods are supported, and the body element exists (using a double-check
            including document.body, for the benefit of older moz builds [eg ns7.1] in which
            getElementsByTagName('body')[0] is undefined, unless this script is in the body section) */
            if (typeof document.getElementsByTagName !== 'undefined' && (document.getElementsByTagName('body')[0] !== null || document.body !== null)) {
                /* Call custom function. */
                if (this.DOMContentLoadedCustom()) {
                    this.run();
                }
                else {
                    /* Not ready yet, wait a little more. */
                    setTimeout(this.name + ".schedule()", 250);
                }
            }
        }
        return true;
    },

    init: function () {
        /* If addEventListener supports the DOMContentLoaded event.*/
        if (document.addEventListener) {
            document.addEventListener("DOMContentLoaded", function () { domReadyEvent.run(); }, false);
        }

        /* Schedule to run the init function.*/
        setTimeout("domReadyEvent.schedule()", 100);

        function run() {
            domReadyEvent.run();
        }

        /* Just in case window.onload happens first, add it to onload using an available method.*/
        if (typeof addEvent !== "undefined") {
            addEvent(window, "load", run);
        }
        else if (document.addEventListener) {
            document.addEventListener("load", run, false);
        }
        else if (typeof window.onload === "function") {
            var oldonload = window.onload;
            window.onload = function () {
                domReadyEvent.run();
                oldonload();
            };
        }
        else {
            window.onload = run;
        }

        /* for Internet Explorer */
        /*@cc_on
        @if (@_win32 || @_win64)
        document.write("<script id=__ie_onload defer src=\"//:\"><\/script>");
        var script = document.getElementById("__ie_onload");
        script.onreadystatechange = function () {
            if (this.readyState == "complete") {
                domReadyEvent.run(); // call the onload handler
            }
        };
        @end
        @*/
    }
};

var domReady = function (handler) { domReadyEvent.add(handler); };
domReadyEvent.init();


/* Create ImageFlow instances when the DOM structure has been loaded */
domReady(function ()
{
    var instanceOne = new ImageFlow();
    instanceOne.init({ ImageFlowID: 'starsIF',
        slider: false,
        startID: Number($("#S_Num").val()) + 1
    });
});
