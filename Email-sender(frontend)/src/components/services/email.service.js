import { customAxios } from "./helper";

export async function sendEmail(emailData) {
  try {
    const result = (await customAxios.post(`/email/send`, emailData)).data;
    return result;
  } catch (error) {
    console.error("Error sending email:", error);
    throw error; // Pass error to caller
  }
}

export async function sendEmailWithFile(formData) {
  try {
    const result = await customAxios.post('/email/send-with-file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return result.data;
  } catch (error) {
    console.error('Error sending email with file:', error);
    throw error;
  }
}
