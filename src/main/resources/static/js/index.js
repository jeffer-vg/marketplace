// JavaScript for the carousel
document.addEventListener("DOMContentLoaded", () => {
    const slides = document.querySelectorAll('.carousel-slide');
    const prevBtn = document.getElementById('prev');
    const nextBtn = document.getElementById('next');
    const indicators = document.querySelectorAll('.indicator');

    let currentIndex = 0;

    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.classList.toggle('active', i === index);
            indicators[i].classList.toggle('active', i === index);
        });
    }

    prevBtn.addEventListener('click', () => {
        currentIndex = (currentIndex === 0) ? slides.length - 1 : currentIndex - 1;
        showSlide(currentIndex);
    });

    nextBtn.addEventListener('click', () => {
        currentIndex = (currentIndex === slides.length - 1) ? 0 : currentIndex + 1;
        showSlide(currentIndex);
    });

    indicators.forEach((indicator, i) => {
        indicator.addEventListener('click', () => {
            currentIndex = i;
            showSlide(currentIndex);
        });
    });

    // Autoplay
    setInterval(() => {
        nextBtn.click();
    }, 8000);
    // Autoplay
    setInterval(() => {
        nextBtn.click();
    }, 8000); // Cambia de slide cada 8 segundos (8000 ms)
});


// Abrir modal
document.getElementById('loginBtn').addEventListener('click', () => {
    document.getElementById('loginModal').style.display = 'block';
});

// Cerrar modal
document.querySelector('.close').addEventListener('click', () => {
    document.getElementById('loginModal').style.display = 'none';
});

// Cerrar al hacer clic fuera del modal
window.addEventListener('click', (e) => {
    if (e.target === document.getElementById('loginModal')) {
        document.getElementById('loginModal').style.display = 'none';
    }
});

// Cambiar entre tabs
const loginTab = document.getElementById('loginTab');
const registerTab = document.getElementById('registerTab');
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');

loginTab.addEventListener('click', () => {
    loginTab.classList.add('active');
    registerTab.classList.remove('active');
    loginForm.style.display = 'block';
    registerForm.style.display = 'none';
});

registerTab.addEventListener('click', () => {
    registerTab.classList.add('active');
    loginTab.classList.remove('active');
    registerForm.style.display = 'block';
    loginForm.style.display = 'none';
});
// Abrir modal al hacer clic en "Únete"
document.getElementById('loginBtn').addEventListener('click', () => {
    document.getElementById('loginModal').style.display = 'block';
});







document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const correo = document.getElementById('correo').value;
  const contrasena = document.getElementById('contrasena').value;
  const mensaje = document.getElementById('mensaje');

  try {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ correo, contrasena }),
    });

    if (!response.ok) {
      // Intentar leer respuesta como texto o json según el content-type
      const contentType = response.headers.get('content-type');
      let errorMsg;

      if (contentType && contentType.includes('application/json')) {
        const errorData = await response.json();
        errorMsg = errorData.message || JSON.stringify(errorData);
      } else {
        errorMsg = await response.text();
      }

      mensaje.textContent = errorMsg || 'Error desconocido';
      mensaje.className = 'error';
      return;
    }

    const data = await response.json();
    mensaje.textContent = `${data.mensaje}, ${data.rol}`;
    mensaje.className = 'success';

    switch (data.rol.toLowerCase()) {
      case 'estudiante':
        window.location.href = 'estudiante.html';
        break;
      case 'comprador':
        window.location.href = 'comprador.html';
        break;
      case 'administrador':
        window.location.href = 'admin.html';
        break;
    }
  } catch (error) {
    mensaje.textContent = 'Error de conexión: ' + error.message;
    mensaje.className = 'error';
  }
});
